package com.honvay.hdms.dms.model.dto;

import com.honvay.hdms.dms.activity.entity.Activity;
import com.honvay.hdms.dms.review.dto.ReviewDto;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author LIQIU
 * created on 2019/2/25
 **/
@Data
public class DocumentDetailDto {

	private DocumentFullDto document;

	private List<ReviewDto> reviews;

	private List<Activity> activities;

	private Set<String> permissions;

	private String token;

}
