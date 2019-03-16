/*   Copyright (c) 2019. 本项目所有源码受中华人民共和国著作权法保护，已登记软件著作权。 *     本项目版权归南昌瀚为云科技有限公司所有，本项目仅供学习交流使用，未经许可不得进行商用，开源（社区版）遵守AGPL-3.0协议。 * */
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
