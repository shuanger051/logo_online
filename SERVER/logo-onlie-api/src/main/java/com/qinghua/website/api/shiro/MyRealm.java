package com.qinghua.website.api.shiro;

import com.qinghua.website.api.common.SessionUser;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.service.SysPermissionService;
import com.qinghua.website.server.service.SysRoleService;
import com.qinghua.website.server.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 自定义Realm
 */
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 为超级管理员添加所有权限
     * @param principals
     * @param permission
     * @return
     */
    @Override
    public  boolean isPermitted(PrincipalCollection principals, String permission){
        String userName = principals.getPrimaryPrincipal().toString();
        SysUserDTO user = sysUserService.getSysUserByUserName(userName);
        if (log.isDebugEnabled()) {
            log.debug("principals is {}, permission is {} ", principals.getPrimaryPrincipal(), permission);
        }
        if (user == null) {
            return false;
        }

        // 超级管理员
        if (user.getIsAdmin().equals(SessionUser.IS_ADMIN_VALUE)) {
            return true;
        }

        // 权限认证
        boolean permitted = super.isPermitted(principals, permission);
        if (log.isDebugEnabled()) {
            log.debug("permitted is {} ", permitted);
        }

        return permitted;
    }

    /**
     * 为超级管理员添加所有角色
     * @param principals
     * @param roleIdentifier
     * @return
     */
    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        if (log.isDebugEnabled()) {
            log.debug("principals is {}, roleIdentifier is {} ", principals.getPrimaryPrincipal(), roleIdentifier);
        }
        String userName = principals.getPrimaryPrincipal().toString();
        SysUserDTO user = sysUserService.getSysUserByUserName(userName);
        if (user == null) {
            return false;
        }
        // 超级管理员
        if (user.getIsAdmin().equals(SessionUser.IS_ADMIN_VALUE)) {
            return true;
        }
        boolean hasRole = super.hasRole(principals, roleIdentifier);
        if (log.isDebugEnabled()) {
            log.debug("hasRole is {} ", hasRole);
        }
        return hasRole;
    }

    /**
     * 为当前登录成功的用户授予权限和分配角色。
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){

        // 获取用户名
        String username = (String) principalCollection.getPrimaryPrincipal();
        SysUserDTO user = sysUserService.getSysUserByUserName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //设置角色信息
        List<String> roleList = sysRoleService.getRoleListByUserId(user.getId());
        Set<String> roleSet = new HashSet<>();
        roleSet.addAll(roleList);
        authorizationInfo.setRoles(roleSet);
        //设置权限信息
        List<String> permsList = sysPermissionService.getPermsStringByUserId(user.getId());
        Set<String> permsSet = new HashSet<>();
        permsSet.addAll(permsList);
        authorizationInfo.setStringPermissions(permsSet);

        return authorizationInfo;
    }


    /**
     * 验证当前登录的用户，获取认证信息
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //获取用户名/密码
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        //从数据库查询用户信息
        SysUserDTO sysUserDTO = sysUserService.getSysUserByUserName(username);

        //可以在这里直接对用户名校验,或者调用 CredentialsMatcher 校验
        if (sysUserDTO == null) {
            throw new UnknownAccountException("用户名或密码错误！");
        }

        // 判断是否被禁用
        if (SessionUser.USER_LOCK_STATUS_IS_DISABLED.equals(sysUserDTO.getIsDisabled())) {
            throw new UnknownAccountException("用户已经被禁用，请联系管理员！");
        }

        //这里将 密码对比 注销掉,否则 无法锁定  要将密码对比 交给 密码比较器
        if (!password.equals(sysUserDTO.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }

        //调用 CredentialsMatcher 校验 还需要创建一个类 继承CredentialsMatcher  如果在上面校验了,这个就不需要了
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, sysUserDTO.getPassword(), getName());
        return info;
    }

    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }


}
