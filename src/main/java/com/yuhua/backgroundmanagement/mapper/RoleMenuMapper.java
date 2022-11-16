package com.yuhua.backgroundmanagement.mapper;

import com.yuhua.backgroundmanagement.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YuHua
 * @since 2022-11-13
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Delete("delete from bm_role_menu where role_id=#{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    @Select("select menu_id from bm_role_menu where role_id=#{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
