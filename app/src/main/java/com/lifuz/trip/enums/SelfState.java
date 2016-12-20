package com.lifuz.trip.enums;

/**
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/10/22 18:42
 */
public enum SelfState {

    REGISTER_SUCCESS(201,"注册成功"),
    UPDATE_SUCCESS(202,"修改成功"),
    NO_USER(203,"没有此用户"),
    SIGN_SUCCESS(204,"签到成功"),
    SIGN_FAILURE(205,"签到失败"),
    SIGN_ALREADY(206,"已签到"),
    UPDATE_FAILURE(207,"更新失败"),
    EXPIRED(101,"token已过期"),
    INVALID(102,"token不合法"),
    VALID(103,"有效的token"),
    FILE_TOlARGE(900,"版本文件过大"),
    LATEST_VERSION(901,"最新版本"),
    SAVE_SUCCESS(902,"版本添加成功"),
    SAVE_FAILURE(906,"版本信息添加失败"),
    NEW_VERSION(903,"有新版本"),
    DELETE_SUCCESS(904,"删除成功"),
    DELETE_FAILURE(905,"删除失败");

    private int state;

    private String stateInfo;

    SelfState(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SelfState stateOf(int index) {
        for (SelfState state:values()){
            if (index == state.getState()) {
                return state;
            }
        }

        return null;
    }
}
