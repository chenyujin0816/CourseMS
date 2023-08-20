import HttpService from "../services/HttpService";
import {User} from "../models/User";
import axios from "axios";

class ManageService{

    async getAllUsers(currentPage: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/user/userList",
            {
                params: {'currentPage': currentPage},
                headers: {'Authorization':localStorage.token}
            })
        // console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }
    async getOnlineData(currentPage: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/user/onlineData",
            {
                params: {'currentPage': currentPage},
            })
        // console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async getRegList(currentPage: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/user/regApplies",
            {
                params: {'currentPage': currentPage},
                headers: {'Authorization':localStorage.token}
            })
        // console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async agree(id: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/user/agreeReg",
            {
                params: {'userId': id},
                headers: {'Authorization':localStorage.token}
            })
        // console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async deny(id: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/user/denyReg",
            {
                params: {'userId': id},
                headers: {'Authorization':localStorage.token}
            })
        // console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async freezeUser(id: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/user/freeze",
            {
                headers: {'Authorization':localStorage.token},
                params: {'userId': id}
            })
        // console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async unfreezeUser(id: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/user/unfreeze",
            {
                headers: {'Authorization':localStorage.token},
                params: {'userId': id}
            })
        // console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

}

export default new ManageService();