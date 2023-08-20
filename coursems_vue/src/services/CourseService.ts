import HttpService from "../services/HttpService";
import {User} from "../models/User";
import {Course} from "@/models/Course";
import axios from "axios";

class CourseService{
    async getInfos(){
        const data = await axios.get("http://192.168.163.128:8888/client/course/courses",{
            headers: {'Authorization':localStorage.token}
        });
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async add(courseNumber:string,courseName:string,courseInfo:string,
              courseScore:string,courseStart:string,courseWeek:number,
              coursePwd:string){
        const dto={
            courseNumber:courseNumber,
            courseName:courseName,
            courseInfo:courseInfo,
            courseScore:courseScore,
            courseStart:courseStart,
            courseWeek:courseWeek,
            coursePwd:coursePwd
        }
        const jsonData = await axios.post("http://192.168.163.128:8888/client/course/add",dto);
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async Qdelete(courseinfo:string,teachername:string){
        const jsonData = await axios.post("http://192.168.163.128:8888/client/course/Qdelete",{},{
            headers: {'Authorization':localStorage.token},
            params: {'courseinfo':courseinfo,'teachername':teachername}
        })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async getaddlist(){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/course/addlist",{
            headers: {'Authorization':localStorage.token}
        })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async getdeletelist(){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/course/deletelist",{
            headers: {'Authorization':localStorage.token}
        })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async fixedadd(courseinfo:string,teachername:string){
        const jsonData = await axios.post("http://192.168.163.128:8888/client/course/fixedadd",{},{
            headers: {'Authorization':localStorage.token},
            params: {'courseinfo':courseinfo,'teachername':teachername}
        })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }
    async fixeddelete(courseinfo:string,teachername:string){
        const jsonData = await axios.post("http://192.168.163.128:8888/client/course/fixeddelete",{},{
            headers: {'Authorization':localStorage.token},
            params: {'courseinfo':courseinfo,'teachername':teachername}
        })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async fixed(courseinfo:string,teachername:string){
        const jsonData = await axios.post("http://192.168.163.128:8888/client/course/fixed",{},{
            headers: {'Authorization':localStorage.token},
            params: {'courseinfo':courseinfo,'teachername':teachername}
        })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async getmyindex(){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/course/myindex",{
            headers: {'Authorization':localStorage.token}
        })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async attend(courseinfo:string,teachername:string,coursePwd:string){
        const dto={ coursePwd:coursePwd  }
        const jsonData = await axios.post("http://192.168.163.128:8888/client/attendCourse/attend",
            dto,
            {
                headers: {'Authorization':localStorage.token},
                params: {'courseinfo':courseinfo,'teachername':teachername}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async exit(courseinfo:string){
        const jsonData = await axios.post("http://192.168.163.128:8888/client/attendCourse/exit",{},
            {
                headers: {'Authorization':localStorage.token},
                params: {'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async studentlist(courseinfo:string){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/attendCourse/studentlist",
            {
                headers: {'Authorization':localStorage.token},
                params: {'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async addstudent(courseinfo:string,username:string){
        const dto={ userName:username  }
        const jsonData = await axios.post("http://192.168.163.128:8888/client/attendCourse/addstudent",
            dto,
            {
                headers: {'Authorization':localStorage.token},
                params: {'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async deletestudent(courseinfo:string,username:string){
        const dto = {userName:username}
        const jsonData = await axios.post("http://192.168.163.128:8888/client/attendCourse/deletestudent",
            dto,
            {
                headers: {'Authorization':localStorage.token},
                params: {'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
    }

    async delete(courseinfo:string){
        const jsonData = await axios.post("http://192.168.163.128:8888/client/course/delete",{},
            {
                headers: {'Authorization':localStorage.token},
                params: {'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async mycourse(courseinfo:string,teachername:string){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/course/mycourse",
            {params: {'courseinfo':courseinfo,'teachername':teachername}})
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async getworklist(courseinfo:string,teachername:string,week:any){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/studentcourse/getworklist",
            {
                headers: {'Authorization':localStorage.token},
                params:{'courseinfo':courseinfo,'teachername':teachername,'week':week}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async judgework(courseinfo:string,teachername:string,grade:string,number:string,week:string){
        const jsonData = await axios.post("http://192.168.163.128:8888/client/studentcourse/judgework",{},
            {
                headers: {'Authorization':localStorage.token},
                params:{'courseinfo':courseinfo,'teachername':teachername,'grade':grade,
                    'number':number,'week':week}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async lookgrade(courseinfo:string,teachername:string,week:string){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/studentcourse/lookgrade",
            {
                headers: {'Authorization':localStorage.token},
                params:{'courseinfo':courseinfo,'teachername':teachername,'week':week}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async checkgrade(courseinfo:string,teachername:string){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/attendCourse/check",
            {
                headers: {'Authorization':localStorage.token},
                params:{'courseinfo':courseinfo,'teachername':teachername}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async chart1(courseinfo:string){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/attendCourse/chart1",
            {
                headers: {'Authorization':localStorage.token},
                params:{'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async chart2(courseinfo:string){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/attendCourse/chart2",
            {
                headers: {'Authorization':localStorage.token},
                params:{'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }

    async chart3(courseinfo:string){
        const jsonData = await axios.get("http://192.168.163.128:8888/client/attendCourse/chart3",
            {
                headers: {'Authorization':localStorage.token},
                params:{'courseinfo':courseinfo}
            })
        console.log("请求完成，返回数据：", jsonData.data)
        if(jsonData && jsonData.data)
            return jsonData;
        return null;
    }



}



export default new CourseService();