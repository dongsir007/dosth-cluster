package com.szbsc.rpc.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import com.feign.dto.FeignUser;
import com.feign.rpc.FeignUserRpcService;
import com.szbsc.dosth.entity.User;
import com.szbsc.dosth.repository.UserRepository;
import com.szbsc.rpc.UserService;
import com.szbsc.util.IdGenerator;
import com.szbsc.util.ImgUtil;

@SuppressWarnings("serial")
@RestController
public class FeignUserRpcServiceImpl implements FeignUserRpcService, UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Value("${others.icons}")
	private String icons;
	
	@Value("${others.uploadPath}")
	private String uploadPath;
	
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
		
		String avatar = null;
		if (!StringUtils.isEmpty(feignUser.getAvatar())) {
			avatar = new StringBuilder(this.uploadPath).append(IdGenerator.builder()).append(".jpg").toString();
			ImgUtil.thumbnails(new File(new StringBuilder(this.icons).append(feignUser.getAvatar()).toString()), avatar);
		}
		
		if (userList != null && userList.size() > 0) {
			user = userList.get(0);
			user.setName(feignUser.getUserName());
			user.setAvatar(null);
			if (avatar != null) {
				user.setAvatar(ImgUtil.convertImageToBase64Data(new File(avatar)));
			}
			this.userRepository.saveAndFlush(user);
		} else {
			user = new User();
			user.setId(IdGenerator.builder());
			user.setName(feignUser.getUserName());
			user.setAvatar(null);
			if (avatar != null) {
				user.setAvatar(ImgUtil.convertImageToBase64Data(new File(avatar)));
			}
			this.userRepository.save(user);
		}
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
			result = this.createFeignUser(userList.get(0));
		}
		return result;
	}

	@Override
	public List<FeignUser> list() {
		List<FeignUser> list = new ArrayList<>();
		List<User> userList = this.userRepository.findAll();
		userList.forEach(user -> {
			list.add(this.createFeignUser(user));
		});
		return list;
	}
	
	/**
	 * 创建用户封装对象
	 * @param user
	 * @return
	 */
	private FeignUser createFeignUser(User user) {
		FeignUser result = new FeignUser();
		result.setUserId(user.getId());
		result.setUserName(user.getName());
		result.setAvatar(user.getAvatar());
		return result;
	}

	@Override
	public String batchAddUser(List<FeignUser> userList) {
		for (FeignUser user : userList) {
			addUser(user);
		}
		return "批量添加";
	}
}