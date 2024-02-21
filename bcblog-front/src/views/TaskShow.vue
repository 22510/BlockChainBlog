<template>
    <div class="task">
        <div class="task-massage" style="border: 2px solid #ccc; padding: 1vh; margin: 1vh 0vh; border-radius: 10px;">
            <el-button type="primary" @click="refreshList()">刷新任务清单</el-button>
        </div>
        <div class="task-insert" style="border: 2px solid #ccc; padding: 1vh; margin: 1vh 0vh; border-radius: 10px;">
            <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="6vw" class="applicationForm">
                <el-form-item label="任务名字" prop="name">
                    <el-input v-model="ruleForm.name" />
                </el-form-item>
                <el-form-item label="任务时间" required>
                    <el-col :span="4">
                        <el-form-item prop="date1">
                            <el-date-picker v-model="ruleForm.date1" type="date" placeholder="Pick a date"
                                style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item prop="time1">
                            <el-time-picker v-model="ruleForm.time1" placeholder="Pick a time" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="2" class="text-center">
                        <span class="text-gray-500">至</span>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item prop="date2">
                            <el-date-picker v-model="ruleForm.date2" type="date" placeholder="Pick a date"
                                style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item prop="time2">
                            <el-time-picker v-model="ruleForm.time2" placeholder="Pick a time" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <div class="form-import-remind" style="display: flex;">
                    <el-form-item label="重要程度" prop="importance">
                        <el-select v-model="ruleForm.importance" placeholder="please select your zone">
                            <el-option label="Zone one" value="1" />
                            <el-option label="Zone two" value="2" />
                            <el-option label="Zone three" value="3" />
                            <el-option label="Zone four" value="4" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="任务提醒">
                        <el-switch v-model="ruleForm.remindOpen" />
                        <el-col :span="10" style="margin-left: 1vw;">
                            <el-date-picker v-model="ruleForm.date3" type="date" placeholder="Pick a date"
                                style="width: 100%" />
                        </el-col>
                        <el-col :span="10">
                            <el-time-picker v-model="ruleForm.date3" placeholder="Pick a time" style="width: 100%" />
                        </el-col>
                    </el-form-item>
                </div>
                <el-form-item label="任务描述" prop="desc">
                    <el-input v-model="ruleForm.desc" type="textarea" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm(ruleFormRef)">创建任务</el-button>
                    <el-button type="primary" @click="resetForm(ruleFormRef)">重置内容</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-card class="box-card" shadow="hover" v-for="task in taskList" :key="task">
            <el-descriptions :data="task">
                    <el-descriptions-item label="任务名">{{task.taskName}}</el-descriptions-item>
                    <el-descriptions-item label="任务开始时间">{{task.beginTime}}</el-descriptions-item>
                    <el-descriptions-item label="任务结束时间">{{task.endTime}}</el-descriptions-item>
                    <el-descriptions-item label="重要程度">
                        <el-tag size="small">{{task.importance}}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="任务描述">{{task.Desc}}</el-descriptions-item>
                </el-descriptions>
                <el-button type="primary" class="button" text>任务完成</el-button>
                <el-button type="primary" class="button" text>任务修改</el-button>
                <el-button type="primary" class="button" text>任务删除</el-button>
        </el-card>
    </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import axios from 'axios';

const refreshList = (userName) => {
    axios.get("application/loadDetail", {
        params: {
            userName: userName
        },
        headers: {
            token: localStorage.getItem("token"),
        },
    })
        .then((response) => {
            if (response.data.success == true) {
                applicationDetail.value = response.data.data
            } else {
                alert("获取面试人信息失败");
            }
        })
        .catch(function (error) {
            console.log(error);
        });
}


interface RuleForm {
    name: string
    date1: string
    time1: string
    date2: string
    time2: string
    importance: string
    remindOpen: boolean
    date3: string
    time3: string
    desc: string
}

const ruleFormRef = ref<FormInstance>()
const ruleForm = ref<RuleForm>({
    name: '',
    date1: '',
    time1: '',
    date2: '',
    time2: '',
    importance: '',
    remindOpen: false,
    date3: '',
    time3: '',
    desc: '',
})

const rules = reactive<FormRules<RuleForm>>({
    name: [
        { required: true, message: '请输入任务名字', trigger: 'blur' },
        { min: 1, max: 15, message: '名字长度请在2~15内', trigger: 'blur' },
    ],
    date1: [
        {
            type: 'date',
            required: true,
            message: '请选择开始日期',
            trigger: 'change',
        },
    ],
    time1: [
        {
            type: 'date',
            required: true,
            message: '请选择开始时间',
            trigger: 'change',
        },
    ],
    date2: [
        {
            type: 'date',
            required: true,
            message: '请选择开始日期',
            trigger: 'change',
        },
    ],
    time2: [
        {
            type: 'date',
            required: true,
            message: '请选择开始时间',
            trigger: 'change',
        },
    ],
    importance: [
        {
            required: true,
            message: '请选择任务重要程度',
            trigger: 'change',
        },
    ],
    desc: [
        { required: true, message: '请进行自我介绍', trigger: 'blur' },
    ],
})

const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            console.log('submit!')
        } else {
            console.log('error submit!', fields)
        }
    })
}

const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
}

const taskList = 3;

</script>

<style scoped>
.task {
    width: 85%;
    margin: 0 auto;
    /* 居中状态 */
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.text {
    font-size: 14px;
}

.item {
    margin-bottom: 18px;
}

.box-card {
    /* width: 480px; */
    margin-top: 15px;
    margin-bottom: 15px;
}

.form-import-remind :deep(.el-form-item__content) {
    flex-wrap: nowrap;
}
</style>