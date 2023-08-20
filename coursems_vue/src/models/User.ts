export interface User {
    id?:number;
    username?:string;
    password?:string;
    status?:any, //单字符串不知道用哪个数据类型
    type?:any;
    name?:string;
    class_number?:string;
    time?:string;
    loginStatus?:any;
    //创建时间没放上
}
