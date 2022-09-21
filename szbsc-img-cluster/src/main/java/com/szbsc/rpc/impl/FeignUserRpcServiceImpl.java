package com.szbsc.rpc.impl;

import java.io.File;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feign.dto.FeignUser;
import com.feign.rpc.FeignUserRpcService;
import com.szbsc.dosth.entity.User;
import com.szbsc.dosth.repository.UserRepository;
import com.szbsc.util.IdGenerator;
import com.szbsc.util.ImgUtil;

@SuppressWarnings("serial")
@RestController
@RequestMapping("/user")
public class FeignUserRpcServiceImpl implements FeignUserRpcService {

	@Autowired
	private UserRepository userRepository;
	
	@Value("${others.icons}")
	private String icons;
	@Value("${others.tmpUploadPath}")
	private String tmpUploadPath;
	
	@Override
	public void addUser(FeignUser feignUser) {
		User user = null;
		List<User> userList = this.userRepository.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (StringUtils.isEmpty(feignUser.getUserName())) {
					return null;
				}
				Predicate p1 = criteriaBuilder.equal(root.get("name"), feignUser.getUserName());
				return query.where(p1).getRestriction();
			}
		});
		if (userList != null && userList.size() > 0) {
			user = userList.get(0);
		} else {
			user = new User();
			user.setId(IdGenerator.builder());
		}
		user.setName(feignUser.getUserName());
		
		user.setAvatar(ImgUtil.convertImageToBase64Data(new File(new StringBuilder(this.icons).append(feignUser.getAvatar()).toString())));
		
		this.userRepository.saveAndFlush(user);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public FeignUser getUser(FeignUser feignUser) {
		List<User> userList = this.userRepository.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (StringUtils.isEmpty(feignUser.getUserName())) {
					return null;
				}
				Predicate p1 = criteriaBuilder.like(root.get("name"), "%" + feignUser.getUserName() + "%");
				return query.where(p1).getRestriction();
			}
		});
		FeignUser result = null;
		if (userList != null && userList.size() > 0) {
			result = new FeignUser();
			User user = userList.get(0);
			result.setUserId(user.getId());
			result.setUserName(user.getName());
			result.setAvatar(user.getAvatar());
		}
		return result;
	}
}