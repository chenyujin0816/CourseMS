import HttpService from "../services/HttpService";
import {User} from "../models/User";
import axios from "axios";

class LoginService{
    async login(username:string,password:string) {
        const dto = {
            username: username,
            password: password
        }
        const data = await axios.post("http://192.168.163.128:8888/client/user/login", dto)
        if (data && data.data)
            return data;
        return null;
    }
    async confirmStatus(userId:number){
        const dto = {
            id:userId,
        }
        const data = await axios.post("http://192.168.163.128:8888/client/user/confirmStatus",dto)
        if(data && data.data)
            return data;
        return null;

    }
    async changePassword(userId:number,password:string) {
        const dto = {
            id:userId,
            password: password,
        }
        console.log(dto)
        const data = await axios.post("http://192.168.163.128:8888/client/user/changePassword", dto)
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async confirmLoginTime(userId:number,loginTime:string){
        const dto = {
            id:userId,
            time:loginTime
        }
        const data = await axios.post("http://192.168.163.128:8888/client/user/confirmLoginTime",dto)
        if(data && data.data)
            return data;
        return null;

    }
    async setLoginTime(userId:number,loginTime:string){
        const dto = {
            id:userId,
            time:loginTime,
        }
        const data = await axios.post("http://192.168.163.128:8888/client/user/setLoginTime",dto)
        if(data && data.data)
            return data;
        return null;
    }
    async setLoginStatus(userId:number,loginStatus:any){
        const dto = {
            id:userId,
            loginStatus:loginStatus
        }
        const data = await axios.post("http://192.168.163.128:8888/client/user/setLoginStatus",dto)
        if(data && data.data)
            return data;
        return null;
    }

    async getName(userId: number){
        const data = await axios.get("http://192.168.163.128:8888/client/user/getName",{params:{'userId':userId}})
        if (data && data.data)
            return data;
        return null;
    }

    async getType(userId: number){
        const data = await axios.get("http://192.168.163.128:8888/client/user/getType",{params:{'userId':userId}})
        if (data && data.data)
            return data;
        return null;
    }

    async register(username:string,name:string,password:string,password2:string,type:string) {
        const dto = {
            username: username,
            password: password,
            confirmPwd: password2,
            type: type,
            name: name
        }
        console.log(dto)
        const data = await axios.post("http://192.168.163.128:8888/client/user/register", dto)
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;

    }

    async logout() {
        const data = await axios.get("http://192.168.163.128:8888/client/user/logout")
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }
}

export default new LoginService();