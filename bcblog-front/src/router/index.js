import {createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'index',
        component: () => import("../views/Index.vue")
    },

    {
        path: '/application',
        name: 'application',
        component: () => import("../views/Application.vue")
    },

    {
        path: '/login',
        name: 'login',
        component: () => import("../views/UserLogin.vue")
    },

    {
        path: '/register',
        name: 'register',
        component: () => import("../views/UserRegister.vue")
    },
    {
        path: '/contentManage',
        name: 'contentManage',
        component: () => import("../views/ContentManage.vue"),
        redirect: "/personalStatistics",
        meta: {
            requiresAuth: true
        },
        children: [
            {
                path: '/personalStatistics',
                name: 'personalStatistics',
                component: () => import("../views/PersonalStatistics.vue")
            },
            {
                path: '/personalArticle',
                name: 'personalArticle',
                component: () => import("../views/PersonalArticle.vue")
            },
            {
                path: '/personalBrowseRecord',
                name: 'personalBrowseRecord',
                component: () => import("../views/PersonalBrowseRecord.vue")
            },            
            {
                path: '/personalCollect',
                name: 'personalCollect',
                component: () => import("../views/PersonalCollect.vue")
            },            
            {
                path: '/followVFans',
                name: 'followVFans',
                component: () => import("../views/FollowVFans.vue")
            },
        ],  
    },
    {
        path: '/userPage',
        name: 'userPage',
        component: () => import("../views/UserPage.vue"),
        redirect: "/blogShow",
        children: [
            {
                path: '/blogShow',
                name: 'blogShow',
                component: () => import("../views/BlogShow.vue")
            },
            {
                path: '/ideaShow',
                name: 'ideaShow',
                component: () => import("../views/IdeaShow.vue")
            },            {
                path: '/taskShow',
                name: 'taskShow',
                component: () => import("../views/TaskShow.vue")
            },
            {
                path: '/matchShow',
                name: 'matchShow',
                component: () => import("../views/GameShow.vue")
            },
            {
                path: '/resourceShow',
                name: 'resourceShow',
                component: () => import("../views/ResourceShow.vue")
            },            
        ],      
    },

    {
        path: '/waiting',
        name: 'waiting',
        component: () => import("../views/Waiting.vue")
    },

    {
        path: '/adminLogin',
        name: 'adminLogin',
        component: () => import("../views/AdminLogin.vue")
    },
    {
        path: '/adminRegister',
        name: 'adminRegister',
        component: () => import("../views/AdminRegister.vue")
    },
    {
        path: '/adminPage',
        name: 'adminPage',
        component: () => import("../views/AdminPage.vue"),
        redirect: "/adminIndex",
        meta: {
            requiresAuth: true
        },
        children: [
            {
                path: '/adminIndex',
                name: 'adminIndex',
                component: () => import("../views/AdminPageIndex.vue")
            },
            {
                path: '/adminApplicationManage',
                name: 'adminApplicationManage',
                component: () => import("../views/AdminApplicationManage.vue")
            },
            {
                path: '/adminMemberManage',
                name: 'adminMemberManage',
                component: () => import("../views/AdminMemberManager.vue")
            },            
            {
                path: '/adminUpcomingManage',
                name: 'adminUpcomingManage',
                component: () => import("../views/AdminUpcomingManage.vue")
            },
            {
                path: '/adminWebManage',
                name: 'adminWebManage',
                component: () => import("../views/AdminWebManage.vue")
            },
        ],        
    },
]

const router = createRouter({ //设置为history模式
    base:'bc208',
    history: createWebHistory(),
    routes: routes
})

router.beforeEach(async (to, from, next) => {
    // 如果需要登录
    if (to.meta.requiresAuth) {
        const token = localStorage.getItem("token");
        if (token) {
            next();
        } else {
            alert("尚未登录, 请先登录");
            next({
                path: '/adminLogin',
                query: {redirect: to.fullPath}
            })
        }   
    } else {
        next();
    }
});

export default router;

