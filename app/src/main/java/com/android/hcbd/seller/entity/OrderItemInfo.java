package com.android.hcbd.seller.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guocheng on 2018/4/28.
 */

public class OrderItemInfo implements Serializable {

    /**
     * item : {"amt":10,"car":{"amt":17,"amts":null,"carItemIdHql":"","code":null,"createTime":"2018-04-28T10:57:33","id":98,"itemsIds":null,"menuIds":null,"name":null,"names":"null-null","nums":null,"operId":"dc80da7d-f5f8-4963-8dc6-0f6db784fdfe","operNames":null,"orgCode":null,"paramsObj":null,"pkgs":null,"remark":"","seq":21,"shopId":1,"state":"1","stateContent":"已下单","states":null,"tabCode":"1F02"},"id":4,"menu":{"amt":5,"code":"1C00000001","createTime":"2018-02-28T14:22:13","id":33,"menuItemIdHql":"","menuType":"C","name":"青菜","names":"1C00000001-青菜","num":-1,"operNames":"S0000-Eingabe","orgCode":"027","paramsObj":null,"remark":"青菜","seq":0,"shopId":null,"state":"1","stateContent":"启用","tabCode":null,"type":{"code":null,"createTime":"2018-02-28T14:20:55","id":10,"modelContent":null,"name":"配菜","names":"null-配菜","operNames":"S0000-Eingabe","operate":null,"orgCode":"027","paramsObj":null,"remark":"","shop":{"code":"A000000000001","createTime":"2018-02-28T14:17:36","floor":2,"id":1,"img":null,"modelContent":["商家","/order/shopAction!"],"name":"麻辣汤","names":"A000000000001-麻辣汤","operNames":"S0000-Eingabe","orgCode":"027","paramsObj":null,"qrCode":"WXlNShzTbYX3nej91ndbIA==","remark":null,"room":20,"state":"1","stateContent":"启用","table":20,"upload":null,"uploadContentType":null,"uploadFileName":null,"url":""},"state":"1","stateContent":"启用"},"upload":null,"uploadContentType":null,"uploadFileName":null,"url":"/upload/file/027/S0000/2018/2/28/2018_02_28 14_22_13.png"},"num":2,"pkg":"","state":"1"}
     * menu : {"amt":5,"code":"1C00000001","id":33,"name":"青菜","remark":"青菜","state":"1","typeId":10,"url":"/upload/file/027/S0000/2018/2/28/2018_02_28 14_22_13.png"}
     */

    private ItemBean item;
    private MenuBeanX menu;

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public MenuBeanX getMenu() {
        return menu;
    }

    public void setMenu(MenuBeanX menu) {
        this.menu = menu;
    }

    public static class ItemBean implements Serializable {
        /**
         * amt : 10.0
         * car : {"amt":17,"amts":null,"carItemIdHql":"","code":null,"createTime":"2018-04-28T10:57:33","id":98,"itemsIds":null,"menuIds":null,"name":null,"names":"null-null","nums":null,"operId":"dc80da7d-f5f8-4963-8dc6-0f6db784fdfe","operNames":null,"orgCode":null,"paramsObj":null,"pkgs":null,"remark":"","seq":21,"shopId":1,"state":"1","stateContent":"已下单","states":null,"tabCode":"1F02"}
         * id : 4
         * menu : {"amt":5,"code":"1C00000001","createTime":"2018-02-28T14:22:13","id":33,"menuItemIdHql":"","menuType":"C","name":"青菜","names":"1C00000001-青菜","num":-1,"operNames":"S0000-Eingabe","orgCode":"027","paramsObj":null,"remark":"青菜","seq":0,"shopId":null,"state":"1","stateContent":"启用","tabCode":null,"type":{"code":null,"createTime":"2018-02-28T14:20:55","id":10,"modelContent":null,"name":"配菜","names":"null-配菜","operNames":"S0000-Eingabe","operate":null,"orgCode":"027","paramsObj":null,"remark":"","shop":{"code":"A000000000001","createTime":"2018-02-28T14:17:36","floor":2,"id":1,"img":null,"modelContent":["商家","/order/shopAction!"],"name":"麻辣汤","names":"A000000000001-麻辣汤","operNames":"S0000-Eingabe","orgCode":"027","paramsObj":null,"qrCode":"WXlNShzTbYX3nej91ndbIA==","remark":null,"room":20,"state":"1","stateContent":"启用","table":20,"upload":null,"uploadContentType":null,"uploadFileName":null,"url":""},"state":"1","stateContent":"启用"},"upload":null,"uploadContentType":null,"uploadFileName":null,"url":"/upload/file/027/S0000/2018/2/28/2018_02_28 14_22_13.png"}
         * num : 2
         * pkg :
         * state : 1
         */

        private double amt;
        private CarBean car;
        private int id;
        private MenuBean menu;
        private int num;
        private String pkg;
        private String state;

        public double getAmt() {
            return amt;
        }

        public void setAmt(double amt) {
            this.amt = amt;
        }

        public CarBean getCar() {
            return car;
        }

        public void setCar(CarBean car) {
            this.car = car;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public MenuBean getMenu() {
            return menu;
        }

        public void setMenu(MenuBean menu) {
            this.menu = menu;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getPkg() {
            return pkg;
        }

        public void setPkg(String pkg) {
            this.pkg = pkg;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public static class CarBean implements Serializable {
            /**
             * amt : 17.0
             * amts : null
             * carItemIdHql :
             * code : null
             * createTime : 2018-04-28T10:57:33
             * id : 98
             * itemsIds : null
             * menuIds : null
             * name : null
             * names : null-null
             * nums : null
             * operId : dc80da7d-f5f8-4963-8dc6-0f6db784fdfe
             * operNames : null
             * orgCode : null
             * paramsObj : null
             * pkgs : null
             * remark :
             * seq : 21
             * shopId : 1
             * state : 1
             * stateContent : 已下单
             * states : null
             * tabCode : 1F02
             */

            private double amt;
            private Object amts;
            private String carItemIdHql;
            private Object code;
            private String createTime;
            private int id;
            private Object itemsIds;
            private Object menuIds;
            private Object name;
            private String names;
            private Object nums;
            private String operId;
            private Object operNames;
            private Object orgCode;
            private Object paramsObj;
            private Object pkgs;
            private String remark;
            private int seq;
            private int shopId;
            private String state;
            private String stateContent;
            private Object states;
            private String tabCode;

            public double getAmt() {
                return amt;
            }

            public void setAmt(double amt) {
                this.amt = amt;
            }

            public Object getAmts() {
                return amts;
            }

            public void setAmts(Object amts) {
                this.amts = amts;
            }

            public String getCarItemIdHql() {
                return carItemIdHql;
            }

            public void setCarItemIdHql(String carItemIdHql) {
                this.carItemIdHql = carItemIdHql;
            }

            public Object getCode() {
                return code;
            }

            public void setCode(Object code) {
                this.code = code;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getItemsIds() {
                return itemsIds;
            }

            public void setItemsIds(Object itemsIds) {
                this.itemsIds = itemsIds;
            }

            public Object getMenuIds() {
                return menuIds;
            }

            public void setMenuIds(Object menuIds) {
                this.menuIds = menuIds;
            }

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public String getNames() {
                return names;
            }

            public void setNames(String names) {
                this.names = names;
            }

            public Object getNums() {
                return nums;
            }

            public void setNums(Object nums) {
                this.nums = nums;
            }

            public String getOperId() {
                return operId;
            }

            public void setOperId(String operId) {
                this.operId = operId;
            }

            public Object getOperNames() {
                return operNames;
            }

            public void setOperNames(Object operNames) {
                this.operNames = operNames;
            }

            public Object getOrgCode() {
                return orgCode;
            }

            public void setOrgCode(Object orgCode) {
                this.orgCode = orgCode;
            }

            public Object getParamsObj() {
                return paramsObj;
            }

            public void setParamsObj(Object paramsObj) {
                this.paramsObj = paramsObj;
            }

            public Object getPkgs() {
                return pkgs;
            }

            public void setPkgs(Object pkgs) {
                this.pkgs = pkgs;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public int getShopId() {
                return shopId;
            }

            public void setShopId(int shopId) {
                this.shopId = shopId;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getStateContent() {
                return stateContent;
            }

            public void setStateContent(String stateContent) {
                this.stateContent = stateContent;
            }

            public Object getStates() {
                return states;
            }

            public void setStates(Object states) {
                this.states = states;
            }

            public String getTabCode() {
                return tabCode;
            }

            public void setTabCode(String tabCode) {
                this.tabCode = tabCode;
            }
        }

        public static class MenuBean implements Serializable {
            /**
             * amt : 5.0
             * code : 1C00000001
             * createTime : 2018-02-28T14:22:13
             * id : 33
             * menuItemIdHql :
             * menuType : C
             * name : 青菜
             * names : 1C00000001-青菜
             * num : -1
             * operNames : S0000-Eingabe
             * orgCode : 027
             * paramsObj : null
             * remark : 青菜
             * seq : 0
             * shopId : null
             * state : 1
             * stateContent : 启用
             * tabCode : null
             * type : {"code":null,"createTime":"2018-02-28T14:20:55","id":10,"modelContent":null,"name":"配菜","names":"null-配菜","operNames":"S0000-Eingabe","operate":null,"orgCode":"027","paramsObj":null,"remark":"","shop":{"code":"A000000000001","createTime":"2018-02-28T14:17:36","floor":2,"id":1,"img":null,"modelContent":["商家","/order/shopAction!"],"name":"麻辣汤","names":"A000000000001-麻辣汤","operNames":"S0000-Eingabe","orgCode":"027","paramsObj":null,"qrCode":"WXlNShzTbYX3nej91ndbIA==","remark":null,"room":20,"state":"1","stateContent":"启用","table":20,"upload":null,"uploadContentType":null,"uploadFileName":null,"url":""},"state":"1","stateContent":"启用"}
             * upload : null
             * uploadContentType : null
             * uploadFileName : null
             * url : /upload/file/027/S0000/2018/2/28/2018_02_28 14_22_13.png
             */

            private double amt;
            private String code;
            private String createTime;
            private int id;
            private String menuItemIdHql;
            private String menuType;
            private String name;
            private String names;
            private int num;
            private String operNames;
            private String orgCode;
            private Object paramsObj;
            private String remark;
            private int seq;
            private Object shopId;
            private String state;
            private String stateContent;
            private Object tabCode;
            private TypeBean type;
            private Object upload;
            private Object uploadContentType;
            private Object uploadFileName;
            private String url;

            public double getAmt() {
                return amt;
            }

            public void setAmt(double amt) {
                this.amt = amt;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMenuItemIdHql() {
                return menuItemIdHql;
            }

            public void setMenuItemIdHql(String menuItemIdHql) {
                this.menuItemIdHql = menuItemIdHql;
            }

            public String getMenuType() {
                return menuType;
            }

            public void setMenuType(String menuType) {
                this.menuType = menuType;
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

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getOperNames() {
                return operNames;
            }

            public void setOperNames(String operNames) {
                this.operNames = operNames;
            }

            public String getOrgCode() {
                return orgCode;
            }

            public void setOrgCode(String orgCode) {
                this.orgCode = orgCode;
            }

            public Object getParamsObj() {
                return paramsObj;
            }

            public void setParamsObj(Object paramsObj) {
                this.paramsObj = paramsObj;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public Object getShopId() {
                return shopId;
            }

            public void setShopId(Object shopId) {
                this.shopId = shopId;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getStateContent() {
                return stateContent;
            }

            public void setStateContent(String stateContent) {
                this.stateContent = stateContent;
            }

            public Object getTabCode() {
                return tabCode;
            }

            public void setTabCode(Object tabCode) {
                this.tabCode = tabCode;
            }

            public TypeBean getType() {
                return type;
            }

            public void setType(TypeBean type) {
                this.type = type;
            }

            public Object getUpload() {
                return upload;
            }

            public void setUpload(Object upload) {
                this.upload = upload;
            }

            public Object getUploadContentType() {
                return uploadContentType;
            }

            public void setUploadContentType(Object uploadContentType) {
                this.uploadContentType = uploadContentType;
            }

            public Object getUploadFileName() {
                return uploadFileName;
            }

            public void setUploadFileName(Object uploadFileName) {
                this.uploadFileName = uploadFileName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public static class TypeBean implements Serializable {
                /**
                 * code : null
                 * createTime : 2018-02-28T14:20:55
                 * id : 10
                 * modelContent : null
                 * name : 配菜
                 * names : null-配菜
                 * operNames : S0000-Eingabe
                 * operate : null
                 * orgCode : 027
                 * paramsObj : null
                 * remark :
                 * shop : {"code":"A000000000001","createTime":"2018-02-28T14:17:36","floor":2,"id":1,"img":null,"modelContent":["商家","/order/shopAction!"],"name":"麻辣汤","names":"A000000000001-麻辣汤","operNames":"S0000-Eingabe","orgCode":"027","paramsObj":null,"qrCode":"WXlNShzTbYX3nej91ndbIA==","remark":null,"room":20,"state":"1","stateContent":"启用","table":20,"upload":null,"uploadContentType":null,"uploadFileName":null,"url":""}
                 * state : 1
                 * stateContent : 启用
                 */

                private Object code;
                private String createTime;
                private int id;
                private Object modelContent;
                private String name;
                private String names;
                private String operNames;
                private Object operate;
                private String orgCode;
                private Object paramsObj;
                private String remark;
                private ShopBean shop;
                private String state;
                private String stateContent;

                public Object getCode() {
                    return code;
                }

                public void setCode(Object code) {
                    this.code = code;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public Object getModelContent() {
                    return modelContent;
                }

                public void setModelContent(Object modelContent) {
                    this.modelContent = modelContent;
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

                public String getOperNames() {
                    return operNames;
                }

                public void setOperNames(String operNames) {
                    this.operNames = operNames;
                }

                public Object getOperate() {
                    return operate;
                }

                public void setOperate(Object operate) {
                    this.operate = operate;
                }

                public String getOrgCode() {
                    return orgCode;
                }

                public void setOrgCode(String orgCode) {
                    this.orgCode = orgCode;
                }

                public Object getParamsObj() {
                    return paramsObj;
                }

                public void setParamsObj(Object paramsObj) {
                    this.paramsObj = paramsObj;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public ShopBean getShop() {
                    return shop;
                }

                public void setShop(ShopBean shop) {
                    this.shop = shop;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getStateContent() {
                    return stateContent;
                }

                public void setStateContent(String stateContent) {
                    this.stateContent = stateContent;
                }

                public static class ShopBean implements Serializable {
                    /**
                     * code : A000000000001
                     * createTime : 2018-02-28T14:17:36
                     * floor : 2
                     * id : 1
                     * img : null
                     * modelContent : ["商家","/order/shopAction!"]
                     * name : 麻辣汤
                     * names : A000000000001-麻辣汤
                     * operNames : S0000-Eingabe
                     * orgCode : 027
                     * paramsObj : null
                     * qrCode : WXlNShzTbYX3nej91ndbIA==
                     * remark : null
                     * room : 20
                     * state : 1
                     * stateContent : 启用
                     * table : 20
                     * upload : null
                     * uploadContentType : null
                     * uploadFileName : null
                     * url :
                     */

                    private String code;
                    private String createTime;
                    private int floor;
                    private int id;
                    private Object img;
                    private String name;
                    private String names;
                    private String operNames;
                    private String orgCode;
                    private Object paramsObj;
                    private String qrCode;
                    private Object remark;
                    private int room;
                    private String state;
                    private String stateContent;
                    private int table;
                    private Object upload;
                    private Object uploadContentType;
                    private Object uploadFileName;
                    private String url;
                    private List<String> modelContent;

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getCreateTime() {
                        return createTime;
                    }

                    public void setCreateTime(String createTime) {
                        this.createTime = createTime;
                    }

                    public int getFloor() {
                        return floor;
                    }

                    public void setFloor(int floor) {
                        this.floor = floor;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public Object getImg() {
                        return img;
                    }

                    public void setImg(Object img) {
                        this.img = img;
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

                    public String getOperNames() {
                        return operNames;
                    }

                    public void setOperNames(String operNames) {
                        this.operNames = operNames;
                    }

                    public String getOrgCode() {
                        return orgCode;
                    }

                    public void setOrgCode(String orgCode) {
                        this.orgCode = orgCode;
                    }

                    public Object getParamsObj() {
                        return paramsObj;
                    }

                    public void setParamsObj(Object paramsObj) {
                        this.paramsObj = paramsObj;
                    }

                    public String getQrCode() {
                        return qrCode;
                    }

                    public void setQrCode(String qrCode) {
                        this.qrCode = qrCode;
                    }

                    public Object getRemark() {
                        return remark;
                    }

                    public void setRemark(Object remark) {
                        this.remark = remark;
                    }

                    public int getRoom() {
                        return room;
                    }

                    public void setRoom(int room) {
                        this.room = room;
                    }

                    public String getState() {
                        return state;
                    }

                    public void setState(String state) {
                        this.state = state;
                    }

                    public String getStateContent() {
                        return stateContent;
                    }

                    public void setStateContent(String stateContent) {
                        this.stateContent = stateContent;
                    }

                    public int getTable() {
                        return table;
                    }

                    public void setTable(int table) {
                        this.table = table;
                    }

                    public Object getUpload() {
                        return upload;
                    }

                    public void setUpload(Object upload) {
                        this.upload = upload;
                    }

                    public Object getUploadContentType() {
                        return uploadContentType;
                    }

                    public void setUploadContentType(Object uploadContentType) {
                        this.uploadContentType = uploadContentType;
                    }

                    public Object getUploadFileName() {
                        return uploadFileName;
                    }

                    public void setUploadFileName(Object uploadFileName) {
                        this.uploadFileName = uploadFileName;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public List<String> getModelContent() {
                        return modelContent;
                    }

                    public void setModelContent(List<String> modelContent) {
                        this.modelContent = modelContent;
                    }
                }
            }
        }
    }

    public static class MenuBeanX implements Serializable {
        /**
         * amt : 5.0
         * code : 1C00000001
         * id : 33
         * name : 青菜
         * remark : 青菜
         * state : 1
         * typeId : 10
         * url : /upload/file/027/S0000/2018/2/28/2018_02_28 14_22_13.png
         */

        private double amt;
        private String code;
        private int id;
        private String name;
        private String remark;
        private String state;
        private int typeId;
        private String url;

        public double getAmt() {
            return amt;
        }

        public void setAmt(double amt) {
            this.amt = amt;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
