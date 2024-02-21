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
            <el-form-item label="姓名:" prop="name">
                <el-input v-model="ruleForm.name" />
            </el-form-item>
            <el-form-item label="年级:" prop="grade">
                <el-select v-model="ruleForm.grade" style="width:auto;" placeholder="请选择你的年级">
                    <el-option label="21" value="21" />
                    <el-option label="22" value="22" />
                    <el-option label="23" value="23" />
                    <el-option label="24" value="24" />
                </el-select>
            </el-form-item>
            <el-form-item label="邮箱:" prop="email">
                <el-input v-model="ruleForm.email" />
            </el-form-item>
            <el-form-item label="兴趣" prop="type">
                <el-radio-group v-model="ruleForm.interest">
                    <el-radio :label="1">区块链</el-radio>
                    <el-radio :label="2">前端</el-radio>
                    <el-radio :label="3">后端</el-radio>
                    <el-radio :label="4">宣发/美术</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="密码:" prop="password">
                <el-input type="password" v-model="ruleForm.password" />
            </el-form-item>
            <el-form-item label="验证码:" prop="captcha">
                <el-input v-model="ruleForm.captcha">
                    <template #suffix>
                        <el-button type="primary" @click="getCaptcha(ruleFormRef)" :disabled="buttonDisabled">
                            <!-- 获取验证码 -->
                            {{ buttonDisabled ? ` ${countdownSeconds} 秒后可用` : '获取验证码' }}
                        </el-button>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item class="submitButton">
                <el-button type="primary" @click="Register(ruleFormRef)">
                    注册
                </el-button>
                <el-button type="primary" @click="resetForm(ruleFormRef)">重置内容</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

import { ElMessage, ElMessageBox } from 'element-plus'
import type { Action } from 'element-plus'

import axios from 'axios';
import useRouter from '../router/index'

interface RuleForm {
    name: string
    grade: string
    email: string
    interest: Number
    password: string
    captcha: string
}

const formSize = ref('default')
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
    name: '',
    grade: '',
    email: '',
    interest: 0,
    password: '',
    captcha: '',
})

const checkEmail = (rule: any, value: any, callback: any) => {
    const regEmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
    if (regEmail.test(value)) {
        // True 合法的邮箱
        return callback()
    }
    callback(new Error('请输入合法的邮箱'))
}

const rules = reactive<FormRules<RuleForm>>({
    name: [
        {
            required: true,
            message: '请输入名字',
            trigger: 'blur',
        },
    ],
    grade: [
        {
            required: true,
            message: '请选择你的年级',
            trigger: 'blur',
        },
    ],
    email: [
        {
            required: true,
            message: '请输入邮箱',
            trigger: 'blur',
        },
        {
            validator: checkEmail,
            trigger: 'blur',
        }
    ],
    password: [
        {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
        },
    ],
    interest: [
        {
            type: 'number',
            required: true,
            message: '选择你感兴趣的方向',
            trigger: 'blur',
        },
    ],
})


const open = () => {
    ElMessageBox.alert('注册申请提交成功, 若为协会/工作室成员, 请向管理员邮箱发送邮件说明.', '提示', {
        // if you want to disable its autofocus
        // autofocus: false,
        confirmButtonText: 'OK',
        callback: (action: Action) => {
            ElMessage({
                type: 'success',
                message: `action: ${action}`,
            })
        },
    })
}

let userInterest: string

const Register = async (formEl: FormInstance | undefined) => {
    if (!formEl) return console.error("错误");
    await formEl.validate((valid, fields) => {
        if (valid) {
            switch (ruleForm.interest) {
                case 1:
                    userInterest = "BlockChain"
                    break;
                case 2:
                    userInterest = "FrontEnd"
                    break;
                case 3:
                    userInterest = "BackEnd"
                    break;
                case 4:
                    userInterest = "Publicity/beautify"
                    break;
            }
            axios.post('bc208/register', {
                "username": ruleForm.name,
                "grade": ruleForm.grade,
                "email": ruleForm.email,
                "interest": userInterest,
                "password": ruleForm.password,
                "captcha": ruleForm.captcha,
            })
                .then(function (response) {
                    if (response.data.success == true) {
                        open();
                        setTimeout(() => {
                            router.push('/userpage');
                        }, 2000)
                    } else {
                        ElMessage({
                            message: '注册失败, ' + response.data.errorMsg,
                            type: 'error',
                        })
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

const buttonDisabled = ref(false)
const countdownSeconds = ref(60)

let countdownTimer: any = null; // 定时器

const startCountdown = () => {
    if (!buttonDisabled.value) {
        buttonDisabled.value = true;
        countdownTimer = setInterval(() => {
            countdownSeconds.value -= 1;
            if (countdownSeconds.value <= 0) {
                clearInterval(countdownTimer);
                buttonDisabled.value = false;
                countdownSeconds.value = 10; // 重新设置倒计时时间
            }
        }, 1000); // 每秒更新倒计时
    }
}


const getCaptcha = async (formEl: FormInstance | undefined) => {
    if (!formEl) return console.error("错误");
    await formEl.validate((valid, fields) => {
        if (valid) {
            axios.get('bc208/captcha', {
                params: {
                    email: ruleForm.email
                }
            })
                .then(function (response) {
                    if (response.data.success == true) {
                        ElMessage({
                            message: '获取验证码成功, 请邮箱查看, 有效时间2分钟.',
                            type: 'success',
                        })
                        startCountdown();
                    } else {
                        ElMessage({
                            message: '获取验证码失败, 请重新尝试.',
                            type: 'error',
                        })
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

const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
}

const router = useRouter

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
    margin-top: 80px;
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
    margin-left: 20%;
    font-size: large;
    background-color: black;
}

.toIndex {
    text-decoration: none;
    color: black;
}
</style>