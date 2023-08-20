<template style="z-index: 1">
  <div class="checkOnline" style="z-index: 2"><el-button size="small" type="primary" icon="el-icon-monitor" circle @click="showOnlineData"></el-button>

  <el-dialog title="在线用户(30秒内)" v-model="dialogTableVisible" width="642px">
    <el-table :data="onlineData.records" empty-text="无在线用户" border stripe>
      <el-table-column label="姓名" width="300" prop="name"></el-table-column>
      <el-table-column label="登录时间" width="300" prop="time"></el-table-column>
    </el-table>
    <tr>
      <!-- 分页 -->
      <el-pagination
          @current-change="handleCurrentChange1"
          :current-page="this.onlineData.current"
          page-size="7"
          layout=" prev, pager, next"
          :total="this.onlineData.total"

      ></el-pagination>
    </tr>
  </el-dialog>
  </div>
  <router-view/>
</template>

<script>
import ManageService from "@/services/ManageService";
import LoginService from "@/services/LoginService";

export default {
  data() {
    return {
      onlineData: [],
      onlineAllRecords:[],
      onlineTotal:0,
      dialogTableVisible: false,
    }
  },
  async mounted() {
    this.timer = setInterval(this.confirmStatus, 1000);
    this.timer1 = setInterval(this.updateOnlineData,1000)
    this.timer2 = setInterval(this.setLoginStatus,1000)
    setInterval(this.updateUserStatus,30000)
  },
  beforeDestroy() {
    clearInterval(this.timer)
    clearInterval(this.timer1)
    clearInterval(this.timer2)
  },
  methods:{
    async updateUserStatus() {
      let seconds = new Date().getSeconds()
      if(seconds=="1"||seconds=='2'||seconds=='3')
        for (let i = 0; i < this.onlineData.records.length; i++) {
          await LoginService.setLoginStatus(this.onlineData.records[i].id, '0')
        }

    },
    async updateOnlineData() {
      await this.getOnlineData(1)
      for (let i = 1; i <= this.onlineData.pages; i++) {
        const res = await ManageService.getOnlineData(i);
        this.onlineAllRecords = this.onlineAllRecords.concat(res.data.data.records)
      }
    },
    async confirmStatus() {
      let id = localStorage.id;
      let loginTime = localStorage.time_last_login;
      if(id != null)
      {
      const res = await LoginService.confirmStatus(id)
      if(res.data.data=="1")
      {
        //
      }
      else {
        clearInterval(this.timer);
        this.timer = null;
        this.$alert('账号被封禁！请刷新页面', '警告', {
          confirmButtonText: '确定',
          callback: action => {
            localStorage.clear();
            this.$router.push({path:'/'})
            this.$message.warning("返回登录页")
          },
        })
      }
      const res1 = await LoginService.confirmLoginTime(id,loginTime);
      if(res1.data.data!=loginTime)
      {
        clearInterval(this.timer);
        this.timer = null;
        this.$alert('账号在别处登录！请刷新页面', '警告', {
          confirmButtonText: '确定',
          callback: action => {
            this.$router.push({path:'/'})
            this.$message.warning("返回登录页")
          },
        })
        localStorage.clear();
      }
      }
    },
    async setLoginStatus() {
      let id = localStorage.id
      if (id != null) {
        await LoginService.setLoginStatus(id,'1')
      }
    },
    showOnlineData() {
      this.dialogTableVisible = true
    },
    async getOnlineData(currentPage){
      const res = await ManageService.getOnlineData(currentPage);
      this.onlineData=res.data.data;
      for (let i = 0; i < this.onlineData.records.length; i++) {
        if(this.onlineData.records[i].type == '1')
        {
          this.onlineData.records[i].name += "(学生)"
        }
        else if(this.onlineData.records[i].type == '2')
        {
          this.onlineData.records[i].name += "(教师)"
        }
        else
        {
          this.onlineData.records[i].name += "(管理员)"
        }
      }

    },
    async handleCurrentChange1(newPage){
      this.onlineData.current=newPage
      this.onlineData.orders=[]
      this.onlineData.pages=Math.ceil(this.onlineAllRecords.length/7)
      this.onlineData.records = this.onlineAllRecords.slice(7*(newPage-1),7*newPage)
      this.onlineData.size=7
      this.onlineData.total=this.onlineAllRecords.length
    }
  }
}

</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
.checkOnline{
  position: fixed;
  top: 50vh;
  right: 0;

}
#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
