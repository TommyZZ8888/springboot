package com.www.orm.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.orm.mybatisplus.entity.SysUserEntity;
import com.www.orm.mybatisplus.entity.User;
import com.www.orm.mybatisplus.entity.ViolationEntity;
import com.www.orm.mybatisplus.mapper.UserMapper;
import com.www.orm.mybatisplus.mapper.ViolationMapper;
import com.www.orm.mybatisplus.service.SysUserService;
import com.www.orm.mybatisplus.service.UserService;
import com.www.orm.mybatisplus.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.www.orm.mybatisplus.config.ComplainantEnums.*;

/**
 * @Description UserServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/12/6
 */
@Service
public class ViolationServiceImpl extends ServiceImpl<ViolationMapper, ViolationEntity> implements ViolationService {

	@Autowired
	private SysUserService sysUserService;

	public String getComplainantArType(String value){
		String arName="";
		switch (value) {
			case "WHISTLEBLOWER":
				arName = WHISTLEBLOWER.getNameAr();
				break;
			case "CUSTOMER":
				arName = CUSTOMER.getNameAr();
				break;
			case "EX_EMPLOYEE":
				arName = EX_EMPLOYEE.getNameAr();
				break;
			case "EMPLOYEE":
				arName = EMPLOYEE.getNameAr();
				break;
			case "CONTRACTOR":
				arName = CONTRACTOR.getNameAr();
				break;
			case "OTHER":
				arName = OTHER.getNameAr();
				break;
			default:
				throw new IllegalArgumentException("Unsupported type ");
		}
		return arName;
	}

public List<ViolationEntity> list(){
	QueryWrapper<ViolationEntity> queryWrapper = new QueryWrapper<>();
	queryWrapper.eq("org_id", 1);
	List<ViolationEntity> collect = baseMapper.selectList(queryWrapper);

	HashMap<String , Integer> complainantsRoleMap = new HashMap();
	HashMap<String , Integer> arComplainantsRoleMap = new HashMap();
	HashMap<Integer , Integer> assignedPersonMap = new HashMap();

	for(ViolationEntity violationEntity : collect){
		complainantsRoleMap.merge(violationEntity.getComplainantRole(),1,Integer::sum);
		arComplainantsRoleMap.put(getComplainantArType(violationEntity.getComplainantRole()),complainantsRoleMap.get(violationEntity.getComplainantRole()));
		if(CollectionUtils.isNotEmpty(violationEntity.getAssignedPerson())){
			for(Integer assignedPerson :violationEntity.getAssignedPerson()){
				assignedPersonMap.merge(assignedPerson,1,Integer::sum);
			}
		}
	}
//        arComplainantsRoleMap.put("violationTotal",violationList.size());
//        complainantsRoleMap.put("violationTotal",violationList.size());
	Map<Integer, Integer> topTenSortedMap = assignedPersonMap.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			.limit(10)
			.collect(Collectors.toMap(
					Map.Entry::getKey,
					Map.Entry::getValue,
					(oldValue, newValue) -> oldValue,
					LinkedHashMap::new
			));
	Map<String, Integer> topTenSortedNameMap = new LinkedHashMap<>();
	for(Integer key : topTenSortedMap.keySet()){
		SysUserEntity sysUserEntity = sysUserService.getById(key);
		if(sysUserEntity!=null) {
			topTenSortedNameMap.put(sysUserEntity.getName(), topTenSortedMap.get(key));
		}
	}
	return collect;
}
}
