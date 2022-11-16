package com.yuhua.backgroundmanagement.service;

import com.yuhua.backgroundmanagement.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuHua
 * @since 2022-11-09
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
