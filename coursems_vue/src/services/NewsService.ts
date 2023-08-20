import HttpService from "../services/HttpService";
import {User} from "../models/User";
import axios from "axios";

class NewsService{
    async getNews() {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/news/getAllNews",
            {
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async releaseNews(title: string, courseNumber: string,description: string){
        const dto = {
            title: title,
            content: description,
            courseNumber: courseNumber
        }
        const data = await axios.post(
            "http://192.168.163.128:8888/client/news/releaseNews",
            dto,{
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async releasePublicNews(title: string,description: string){
        const dto = {
            title: title,
            content: description
        }
        const data = await axios.post(
            "http://192.168.163.128:8888/client/news/releasePublicNews",
            dto,{
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

}

export default new NewsService();