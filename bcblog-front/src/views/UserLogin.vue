<template>
    <div class="shell">
        <div class="BCLogo" style="padding-top: 5%;">
            <img style="width: 150px" src="/src/assets/index/BClogo.png" />
            <div style="
            font-size: 60px;
            /* color: #fefefe; */
            text-shadow: 0 0 0.5em #38a1ff, 0 0 0.2em #5c5c5c;
        " class="BCName">
                <router-link to="/" class="toIndex">BlockChain Studio 208</router-link>
            </div>
        </div>
        <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="150px" class="applicationForm" status-icon
            label-position="left">
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="ruleForm.email" />
            </el-form-item>
            <el-form-item label="密码:" prop="password">
                <el-input type="password" v-model="ruleForm.password" />
            </el-form-item>
            <el-form-item class="submitButton">
                <el-button type="primary" @click="Login(ruleFormRef)">
                    登录
                </el-button>
                <el-button type="primary" @click="Register">
                    注册
                </el-button>
                <el-button type="primary" @click="drawerChange = true, qrcodeURL = getQRCode()">
                    微信登录
                </el-button>
            </el-form-item>
        </el-form>

        <el-drawer class="drawer" v-model="drawerChange" direction=ltr size="32vw">
            <img src="../assets/wxLogin.jpg">
        </el-drawer>
    </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onBeforeMount } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import axios from 'axios';
// import { request } from '../request'

import useRouter from '../router/index'
const router = useRouter


interface RuleForm {
    email: string
    password: string
}

const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
    email: '',
    password: '',
})

const rules = reactive<FormRules<RuleForm>>({
    email: [
        {
            required: true,
            message: '请输入邮箱',
            trigger: 'blur',
        },
    ],
    password: [
        {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
        },
    ],
})


const successMessage = () => {
    ElMessage({
        message: '用户登录成功, 正在跳转.',
        type: 'success',
    })
    setTimeout(() => {
        router.push('/userpage');
    }, 3000)
}

const falseMessage = (errorMsg: string) => {
    ElMessage({
        message: '登录失败, ' + errorMsg,
        type: 'error',
    })
}


const Login = async (formEl: FormInstance | undefined) => {
    if (!formEl) return console.error("错误");
    await formEl.validate((valid, fields) => {
        if (valid) {
            axios.post('bc208/login', {
                "email": ruleForm.email,
                "password": ruleForm.password
            })
                .then(function (response) {
                    if (response.data.success == true && response.data.data != null) {
                        localStorage.setItem('token', response.data.data),
                            successMessage()
                    } else {
                        falseMessage(response.data.errorMsg)
                        formEl.resetFields()
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        } else {
            console.log('error submit!', fields)
        }
    })
}

const wxLogin = () => {

}

const Register = () => {
    ElMessage({
        message: '正在前往注册页面.',
        type: 'success',
    })
    setTimeout(() => {
        router.push('/register');
    }, 2000)
}

const drawerChange = ref(false)

// const checkToken =  async () => {
//     const token = localStorage.getItem('token')
//     await axios.get("bc208/checkLogin", {
//             headers: {
//                 token: localStorage.getItem("token"),
//             },
//             })
//             .then(function (response) {
//                 if (response.data.success == true && response.data.data != null) {
//                     localStorage.setItem('token', response.data.data),
//                         successMessage()
//                 } else {
//                 }
//             })
//             .catch(function (error) {
//                 console.log(error);
//             }); 
// }

// const toUserPage = () => {
//     router.push('/userPage');
// }

// onBeforeMount(() => {
//     checkToken()
// })
const qrcodeURL = ref();

const getQRCode = () => {
    axios.get('bc208/wxQRCode')
        .then(function (response) {
            if (response.data.success == true) {
                console.log("生成二维码")
            } else {
                falseMessage(response.data.errorMsg)
            }
        })
        .catch(function (error) {
            console.log(error);
        });
    return "http://39.101.74.9/images/"+ "QRCode.jpg"
}

</script>

<style scoped>
* {
    padding: 0;
    margin: 0;
}

.shell {
    background-image: url("../assets/index/adminlogin.png");
    background-size: cover;
    height: 100vh;
    height: calc(100vh -60px);
    /* overflow-x: hidden; */
    /* 设置当内容溢出块级元素的左右两侧时显示的内容, 或不显示, hidden为不显示溢出的*/
    perspective: 3px;
    /* 观察者与z=0屏幕的距离, 具有三维位置变换的元素产生透视效果 */
}

.shell div {
    position: relative;
    /* 元素在文档中的定位方式, relative元素定位, 不改变页面布局下调整位置*/
    display: flex;
    /* 元素是否被视为块或内联元素, 用于布局, flex弹性盒模型 */
    justify-content: center;
    /* 浏览器如何沿着弹性容器的株洲分配元素之间和周围的空间 */
    align-items: center;
    /* 当页居中? */
    /* font-style: 30px; */
    font-family: "Times New Roman", Times, serif;
    letter-spacing: 2px;
}

.el-form {
    margin-top: 100px;
    width: 40%;
    margin-left: 30%;
}

/* 
.el-form-item :deep() .label {
    font-size: large;
} */

.el-form-item {
    padding: 15px;
}

.applicationForm :deep() .el-form-item__label {
    font-size: large;
}

.submitButton .el-button {
    margin-left: 12%;
    font-size: large;
    background-color: #38a1ff;
    ;
}

.toIndex {
    text-decoration: none;
    color: black;
}
</style>