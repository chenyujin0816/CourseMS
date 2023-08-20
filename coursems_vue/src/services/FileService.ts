import HttpService from "../services/HttpService";
import {User} from "../models/User";
import axios from "axios";

class FileService{
    async getFileList(type: string,courseName:string,week:string){
        const data = await axios.get("http://192.168.163.128:8888/client/file/getFiles",
            {
                params:{
                    'type':type,
                    'courseName': courseName,
                    'week': week
                }
            })
        if (data && data.data)
            return data;
        return null;
    }

    async getAssign(type: string,courseName:string,week:string){
        const data = await axios.get("http://192.168.163.128:8888/client/file/getAssign",
            {
                headers: {'Authorization':localStorage.token},
                params:{
                    'type':type,
                    'courseName': courseName,
                    'week': week
                }
            })
        if (data && data.data)
            return data;
        return null;
    }

    async getAllAssign(type: string,courseName:string,week:string){
        const data = await axios.get("http://192.168.163.128:8888/client/file/getAllAssign",
            {
                headers: {'Authorization':localStorage.token},
                params:{
                    'type':type,
                    'courseName': courseName,
                    'week': week
                }
            })
        if (data && data.data)
            return data;
        return null;
    }

    async deleteFile(type: string,courseName:string,week:string,name:string){
        console.log(type,courseName,week,name)
        const data = await axios.get("http://192.168.163.128:8888/client/file/delete",
            {
                params:{
                    'type':type,
                    'courseName': courseName,
                    'week': week,
                    'name': name
                }
            })
        if (data && data.data)
            return data;
        return null;
    }

    async deleteAssign(type: string,courseName:string,week:string,name:string){
        console.log(type,courseName,week,name)
        const data = await axios.get("http://192.168.163.128:8888/client/file/deleteAssign",
            {
                headers: {'Authorization':localStorage.token},
                params:{
                    'type':type,
                    'courseName': courseName,
                    'week': week,
                    'name': name
                }
            })
        if (data && data.data)
            return data;
        return null;
    }

    async download(type: string,courseName:string,week:string,name:string){

        const data = await axios.get("http://192.168.163.128:8888/client/file/download",
            {
                params:{
                    'type':type,
                    'courseName': courseName,
                    'week': week,
                    'name': name
                }
            })
        if (data && data.data)
            return data;
        return null;
    }
}

export default new FileService();