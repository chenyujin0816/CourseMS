package cn.edu.bit.shiro;

import cn.edu.bit.entity.User;
import cn.edu.bit.service.UserService;
import cn.edu.bit.utils.JwtUtils;
import cn.hutool.core.bean.BeanUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken=(JwtToken) token;
        String userId=jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        User user=userService.getById(Long.valueOf(userId));
        if(user==null){
            throw new UnknownAccountException("账户不存在");
        }
        if(user.getStatus()=='0'){
            throw new LockedAccountException("账户已被冻结");
        }
        AccountProfile profile=new AccountProfile();
        BeanUtil.copyProperties(user,profile);

        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }
}
