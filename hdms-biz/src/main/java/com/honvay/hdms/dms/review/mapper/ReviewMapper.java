package com.honvay.hdms.dms.review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.dms.review.dto.ReviewDto;
import com.honvay.hdms.dms.review.entity.Review;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LIQIU
 * created on 2019/2/25
 **/
public interface ReviewMapper extends BaseMapper<Review> {

	@Select("select t.id as id ,"
			+ "t.content as content,"
			+ "t.user_id as reviewer,"
			+ "t.review_date as reviewDate,"
			+ "user.name as name,"
			+ "user.avatar as avatar "
			+ "from hdms_review t "
			+ " left join hdms_user user on user.id = t.user_id"
			+ " where t.document_id = #{documentId}"
			+ " order by t.review_date")
	List<ReviewDto> findByDocumentId(@Param("documentId") Integer documentId);

}
