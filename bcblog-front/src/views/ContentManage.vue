<template>
    <div class="common-layout">
      <el-container style="height: 100vh;">
        <el-header>
          <el-row :gutter="20">
            <el-col :span="4">
              <div style="
                          font-size: 0.9rem;
                          color: #fefefe;
                          /* text-shadow: 0 0 0.5em #0a34f0, 0 0 0.2em #5c5c5c; */
                          float: left;
                          padding-left: 1vw;
                          " class="BCName">
                <router-link to="/userPage" class="toIndex">BlockChain Studio 208</router-link>
              </div>
            </el-col>
            <el-col :span="16" style="color: white; text-align: center;">
              个人内容管理
            </el-col>
            <el-col :span="4">
              <el-dropdown>
                <el-button type="primary">
                  User<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item>修改密码</el-dropdown-item>
                    <el-dropdown-item>账号退出</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-col>
          </el-row>
        </el-header>
        <el-container >
          <el-aside >
            <el-menu default-active="1" class="el-menu-vertical-demo" style="border-radius: 10px;">
              <el-menu-item index="1">
                <router-link class="router-link" to="/personalStatistics">数据统计</router-link>
              </el-menu-item>
              <el-menu-item index="2">
                <router-link class="router-link" to="/personalArticle">文章</router-link>    
              </el-menu-item>
              <el-menu-item index="3">
                <router-link class="router-link" to="/personalBrowseRecord">浏览记录</router-link>
              </el-menu-item>
              <el-menu-item index="4">
                <router-link class="router-link" to="/followVFans">关注与粉丝</router-link>
              </el-menu-item>
              <el-menu-item index="5">
                <router-link class="router-link" to="/personalCollect">文章收藏</router-link>
              </el-menu-item>
            </el-menu>
          </el-aside>
          <el-main style="border-radius: 10px;">
            <!-- 路由页面 -->
            <router-view>    
  
            </router-view>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </template>
  
  <style scoped>
  *{
    margin: 0;
  
  }
  .common-layout{
    background-color: #99CCFF;
  }
  
  .el-aside {
    padding-top: 12vh;
    padding-left: 0.8vw;
    padding-right: 0.6vw;
    width: 10vw;
    height: 100%;
  }
  
  .el-main{
    margin: 1vw;
    /* background-color: rgb(245, 206, 206); */
    padding: 0;
  }
  
  
  .el-row {
    align-items: center;
    vertical-align: middle;
    padding-top: 10px;
  }
  
  .el-header {
    background-color: #6699CC;
  }
  
  .router-link{
    text-decoration: none; /* 清除下划线 */
    color: inherit; /* 使用继承的颜色 */
  }
  
  .toIndex {
      text-decoration: none;
      color: #00ffff;
  }
  </style>
    
  