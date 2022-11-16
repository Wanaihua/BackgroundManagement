package com.yuhua.backgroundmanagement.mapper;

import com.yuhua.backgroundmanagement.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YuHua
 * @since 2022-11-09
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from bm_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}
