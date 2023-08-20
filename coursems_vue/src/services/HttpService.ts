import axios, {AxiosInstance} from "axios";
import {JsonResult} from "../models/JsonResult";

class HttpService{
    axiosInstance: AxiosInstance;
    constructor() {
        this.axiosInstance = axios.create({
            baseURL: "/api",
            timeout: 50000,
        });
        this.axiosInstance.interceptors.request.use((req) => {//发起请求之前执行
            //进行请求前置操作
            //显示loading等
            //构造请求体
            console.log("22")
            req.data=req.params;
            console.log("请求前置，参数为：", req.data);
            if(localStorage.token) {
                req.headers = {
                    Authorization: localStorage.token
                }
                console.log(req)
            }
            console.log(req.headers);
            // const reqParams = {...req.params, id: "1"};
            // req.params = reqParams;
            // const reqHeaders = {...req.headers, test: "123refih4u9oio"};
            // req.headers = reqHeaders;
            return req;
        });
        this.axiosInstance.interceptors.response.use((resp) => {//完成请求之后
            //进行后置操作
            //是请求成功不是业务操作成功
            const jsonResult: JsonResult<any> = resp.data;
            console.log("请求完成，返回数据：", jsonResult);
            if (jsonResult && (jsonResult.code == "200"||jsonResult.code=="000000")) {
                const data = jsonResult.data ? jsonResult.data : null;
                resp.data = jsonResult.data;
                return resp;
            } else {
                resp.data=null;
                return resp;
            }
        });
    }

    async get<T>(uri: string, params: any): Promise<T | null> {//发起GET请求

        return await this.request(uri, "GET", params, params);
    }

    async post<T>(uri: string, params: any): Promise<T | null> {//发起POST请求

        return await this.request(uri, "POST",  params, params);
    }

    private async request<T>(url: string, method: "GET" | "POST", data: any, params: any): Promise<T | null> {
        const resp = await this.axiosInstance.request<T>({params, data, url, method}).catch((error) => {
            console.log("请求错误", error);
            return {data: null};
        });
        return resp.data;
    }
}

export default new HttpService();
