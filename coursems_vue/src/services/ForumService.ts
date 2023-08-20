import HttpService from "../services/HttpService";
import {User} from "../models/User";
import axios from "axios";

class ForumService{
    async getPosts() {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/forumPost/posts",
            {
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async getPost(id: number) {
        const data = await axios.get("http://192.168.163.128:8888/client/forumPost/post/"+id)
        if (data && data.data)
            return data;
        return null;
    }

    async getComments(postId: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/commentPost/comments",
            {
                params: {'postId': postId}
            })
        if (data && data.data)
            return data;
        return null;
    }

    async getReplies(commentId: number) {
        const data = await axios.get(
            "http://192.168.163.128:8888/client/commentPost/replies",
            {
                params: {'commentId': commentId}
            })
        if (data && data.data)
            return data;
        return null;
    }

    async post(title:string,description:string,creatorId:number){
        const dto = {
            title: title,
            description: description,
            creatorId: creatorId,
            // createTime: currentDate
        }
        console.log("dto",dto)
        const data = await axios.post(
            "http://192.168.163.128:8888/client/forumPost/editPost",
            dto,
            {
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async comment(content:string,commentatorId:number,rootId:number){
        const dto = {
            content: content,
            commentatorId: commentatorId,
            rootId: rootId
        }
        console.log("dto",dto)
        const data = await axios.post(
            "http://192.168.163.128:8888/client/commentPost/postComment",
            dto,
            {
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async reply(content:string,toId:number,parentId:number){
        const dto = {
            content: content,
            toId: toId,
            parentId: parentId
        }
        console.log("dto",dto)
        const data = await axios.post(
            "http://192.168.163.128:8888/client/commentPost/postReply",
            dto,
            {
                headers: {'Authorization':localStorage.token}
            })
        console.log("请求完成，返回数据：", data.data);
        if (data && data.data)
            return data;
        return null;
    }

    async deletePost(id: number) {
        const data = await axios.get("http://192.168.163.128:8888/client/forumPost/deletePost/",
            {
                params: {'postId': id},
                headers: {'Authorization':localStorage.token}
            })
        if (data && data.data)
            return data;
        return null;
    }
}

export default new ForumService();