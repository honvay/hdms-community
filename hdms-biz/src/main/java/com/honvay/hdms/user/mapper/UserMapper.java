package com.honvay.hdms.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.hdms.user.domain.User;
import com.honvay.hdms.user.model.UserUpdateVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author LIQIU
 * created on 2019/2/23
 **/
public interface UserMapper extends BaseMapper<User> {

	/**
	 * @param id
	 * @return
	 */
	@Select("select t.id,t.username,t.name,t.email,t.phone_number,t.department_id,d.name as department_name" +
			" from hdms_user t " +
			"  left join hdms_department d on d.id = t.department_id" +
			"  where t.id = #{id}")
	UserUpdateVo getFullById(@Param("id") Integer id);

	@Select("select count(*) from hdms_user t")
	Long getTotalUser();
}
