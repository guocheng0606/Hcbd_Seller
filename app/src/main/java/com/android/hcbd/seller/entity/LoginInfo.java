package com.android.hcbd.seller.entity;

import java.util.List;

/**
 * Created by guocheng on 2018/3/29.
 */

public class LoginInfo {
    private String token;
    private UserInfo userInfo;
    private List<menuInfo> menuList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<menuInfo> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<menuInfo> menuList) {
        this.menuList = menuList;
    }

    public static class UserInfo{

        /**
         * accountIds : 1
         * accountPermit : false
         * code : S0000
         * id : 1
         * loginTime : 2018-03-29T09:41:04
         * menuPath : null
         * millis : 1522287658152
         * name : Eingabe
         * names : S0000-Eingabe
         * orgCode : 027
         * orgName : 华创北斗总公司
         * permit : true
         */

        private String accountIds;
        private boolean accountPermit;
        private String code;
        private int id;
        private String loginTime;
        private Object menuPath;
        private long millis;
        private String name;
        private String names;
        private String orgCode;
        private String orgName;
        private boolean permit;

        public String getAccountIds() {
            return accountIds;
        }

        public void setAccountIds(String accountIds) {
            this.accountIds = accountIds;
        }

        public boolean isAccountPermit() {
            return accountPermit;
        }

        public void setAccountPermit(boolean accountPermit) {
            this.accountPermit = accountPermit;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public Object getMenuPath() {
            return menuPath;
        }

        public void setMenuPath(Object menuPath) {
            this.menuPath = menuPath;
        }

        public long getMillis() {
            return millis;
        }

        public void setMillis(long millis) {
            this.millis = millis;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNames() {
            return names;
        }

        public void setNames(String names) {
            this.names = names;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public boolean isPermit() {
            return permit;
        }

        public void setPermit(boolean permit) {
            this.permit = permit;
        }
    }

    public static class menuInfo{

        private String code;
        private Object id;
        private String name;
        private String names;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNames() {
            return names;
        }

        public void setNames(String names) {
            this.names = names;
        }
    }


}
