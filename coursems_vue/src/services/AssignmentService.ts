import HttpService from "../services/HttpService";
import {User} from "../models/User";
import {Course} from "@/models/Course";
import axios from "axios";

class AssignmentService{
    async getDetail(courseId: number,week: number){
        const data = await axios.get("http://192.168.163.128:8888/client/assignment/assign",{
            headers: {'Authorization':localStorage.token},
            params:{
                'courseId': courseId,
                'week': week
            }
        });
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async release(title:string,description: string,courseId: number,deadline: string,week: number){
        const dto={
            title: title,
            description: description,
            courseId: courseId,
            deadline: deadline,
            week: week,
        }
        const jsonData = await axios.post("http://192.168.163.128:8888/client/assignment/createAssignment",
            dto,
            {
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

}


export default new AssignmentService();