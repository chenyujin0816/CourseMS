import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Discuss from '../views/Discuss.vue'
import ReleaseHomework from '../views/ReleaseHomework.vue'
import Register from '../views/Register.vue'
import HelloWorld from '../views/HelloWorld.vue'
import Search from '../views/Search.vue'
import My from '../views/My.vue'
import Message from '../views/Message.vue'
import Manage from '../views/Manage.vue'
import Detaildis from '../views/Detaildis.vue'
import Newdis from '../views/Newdis.vue'
import Course from '../views/Course.vue'
import TmS from '../views/TmS.vue'
import Newcourse from  '../views/Newcourse.vue'
import Score from '../views/Score.vue'
import Download from '../views/Download.vue'
import Homework from '../views/Homework.vue'
import Handin from '../views/Handin.vue'
import News from '../views/News.vue'
import Chart from '../views/Chart.vue'
import Information from "@/views/Information.vue";


const routes = [
  {
    path: '/',
    name: 'Index',
    redirect:{name:'login'}
  },
  {
    path: '/information',
    name:'Information',
    component: Information
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/regis',
    name: 'Regis',
    component: Register
  },
  {
    path: '/home',
    name: 'Home',
    component: HelloWorld,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/discuss',
    name: 'Discuss',
    component: Discuss,
    meta:{
      requireAuth:true
    }
  },
  // 论坛详情页和发帖
  {
    path: '/discuss/detail',
    name: 'Detail',
    component: Detaildis,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/discuss/newdis',
    name: 'Newdis',
    component: Newdis,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/search',
    name: 'Search',
    component: Search,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/my',
    name: 'My',
    component: My,
    meta:{
      requireAuth:true
    }
  },
  //老师申请新建课程
  {
    path: '/my/newcourse',
    name: 'Newcourse',
    component: Newcourse,
    meta:{
      requireAuth:true
    }
  },
  // 课程详情页
  {
    path: '/my/course',
    name: 'Course',
    component: Course,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/my/course/set',
    name: 'Set',
    component: ReleaseHomework,
    meta:{
      requireAuth:true
    }
  },
  // 老师管理课程的学生
  {
    path: '/my/course/tms',
    name: 'Tms',
    component: TmS,
    meta:{
      requireAuth:true
    }
  },
  // 作业详情页
  {
    path: '/my/course/homework',
    name: 'Homework',
    component: Homework,
    meta:{
      requireAuth:true
    }
  },
  // 老师评分
  {
    path: '/my/course/homework/score',
    name: 'Score',
    component: Score,
    meta:{
      requireAuth:true
    }
  },
  // 课件下载页
  {
    path: '/my/course/dld',
    name: 'Dls',
    component: Download,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/message',
    name: 'Message',
    component: Message,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/manage',
    name: 'Manage',
    component: Manage,
    meta:{
      requireAuth:true
    }
  },
  {
    path: '/my/course/chart',
    name: 'Chart',
    component: Chart,
    meta:{
      requireAuth:true
    }
  },
  // 提交作业
  {
    path: '/hand',
    name: 'Hand',
    component: Handin,
    meta:{
      requireAuth:true
    }
  },
  // 发布新闻
  {
    path: '/news',
    name: 'News',
    component: News,
    meta:{
      requireAuth:true
    }
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
