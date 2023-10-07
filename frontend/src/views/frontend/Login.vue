<template>
    <div class="main" v-if="show">
        <div v-if="loginView">
            <el-form ref="loginForm" :model="loginForm" :rules="loginRulues" label-width="80px" class="login-box">
                <h3 class="login-title">欢迎登录</h3>
                <el-form-item label="用户名" prop="userName">
                    <el-input type="text" placeholder="请输入账号" v-model="loginForm.userName" />
                </el-form-item>
                <el-form-item label="密码" prop="userPassword">
                    <el-input type="password" placeholder="请输入密码" show-password v-model="loginForm.userPassword" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="onLogin('loginForm')">登录</el-button>
                    <el-button type="primary" class="register-button" v-on:click="onRegist()">注册新用户</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div v-else>
            <el-form ref="registeForm" :model="registeForm" :rules="registeRules" label-width="80px" class="login-box"
                label-position="right">
                <h3 class="login-title">注册新用户</h3>
                <el-form-item label="姓名" prop="userName">
                    <el-input v-model="registeForm.userName"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="userPassword">
                    <el-input v-model="registeForm.userPassword" show-password></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="userSex">
                    <el-radio-group v-model="registeForm.userSex">
                        <el-radio label="男"></el-radio>
                        <el-radio label="女"></el-radio>
                        <el-radio label="其他"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="年龄" prop="userAge">
                    <el-input v-model="registeForm.userAge"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="userTel">
                    <el-input v-model="registeForm.userTel"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="userEmail">
                    <el-input v-model="registeForm.userEmail"></el-input>
                </el-form-item>
                <el-form-item label="个人介绍" prop="userDesc">
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 3 }" placeholder="请输入内容"
                        v-model="registeForm.userDesc"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmitRegist('registeForm')">注 册</el-button>
                    <el-button @click="onCancelRegist()">取 消</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>

export default {
    name: "Login",
    data() {
        let checkAge = (rule, value, callback) => {
            let reg = /^(?:[1-9][0-9]?|1[01][0-9]|120)$/; //年龄是1-120之间有效
            if (value == undefined) {
                callback([new Error("帐号输入不合法")]);
            } else {
                if (value && value.length > 0) {
                    if (!reg.test(value)) {
                        callback([new Error("年龄输入不合法")]);
                    } else {
                        callback();
                    }
                } else if (value.length == 0) {
                    callback();
                } else {
                    callback(new Error("年龄输入不合法"));
                }
            }
        }
        return {
            show: true,
            loginView: true,
            userName: "",
            loginForm: {
                userName: "",
                userPassword: "",
            },
            registeForm: {
                userName: "",
                userPassword: "",
                userAge: null,
                userSex: "",
                userTel: "",
                userEmail: "",
                userDesc: "",
            },
            loginRulues: {
                userName: [{
                    required: true,
                    message: "账号不可为空",
                    trigger: "blur"
                },],
                userPassword: [{
                    required: true,
                    message: "密码不可为空",
                    trigger: "blur"
                },],
            },
            registeRules: {
                userName: [{
                    required: true,
                    message: "用户名为必填项",
                    trigger: "blur",
                }],
                userPassword: [{
                    required: true,
                    message: "密码为必填项",
                    trigger: "blur",
                },
                {
                    min: 6,
                    max: 30,
                    message: "密码长度在6-30之间",
                    trigger: "blur",
                }],
                userSex: [
                    { required: true, message: '请选择性别', trigger: 'change' }
                ],
                userAge: [{
                    required: true,
                    message: '请填写合法年龄',
                    validator: checkAge,
                    trigger: 'blur',
                },
                {
                    pattern: /^-?\d+\.?\d*$/,
                    message: '必须为数字',
                    trigger: 'blur',
                }],
                userTel: [{
                    pattern: /^1[3456789]\d{9}$/,
                    message: '手机号码格式不正确',
                    trigger: 'blur'
                }],
                userEmail: [{
                    pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/,
                    message: '邮箱格式不正确',
                    trigger: 'blur'
                }]
            }
        };
    },
    methods: {
        onRegist() {
            this.loginView = false;
        },
        onCancelRegist() {
            this.loginView = true;
        },
        onLogin(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.$store
                        .dispatch("login", this.loginForm)
                        .then(() => {
                            this.gotohome()
                            this.$store
                            .dispatch("getUserInfo", this.$store.state)
                            .then(()=>{
                                localStorage.setItem('store', JSON.stringify(this.$store.state))
                            })
                        })
                        .catch((error) => {
                            if (error !== "error") {
                                this.$message({
                                    message: error,
                                    type: "error",
                                    showClose: true,
                                });
                            }
                        });
                    
                }
            });
        },
        onSubmitRegist(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                this.$store.
                    dispatch("register",this.registeForm)
                    .then(()=>{
                        this.gotohome()
                        this.$store.dispatch("getUserInfo", this.$store.state)
                    })
                    .catch((error)=>{
                        if (error !== "error") {
                            this.$message({
                                message: error,
                                type: "error",
                                showClose: true,
                            });
                        }
                    });
                }
            });
        },
        gotohome() {
            this.show = !this.show
            setTimeout(() => {
                this.$router.push("/")
            }, 200);
        },

    },
};
</script>

<style lang="scss" scoped>
.main {
    width: 100%;
    height: 100%;
    position: fixed;
    background-size: 100% 100%;
    background-image: url('../../assets/login-background.jpg');
}

.login-box {
    backdrop-filter: blur(10px);
    background-color: rgba(255, 255, 255, 0.3);
    // border: 1px solid #dcdfe6;
    // background-color: transparent;
    width: 500px;
    margin: 80px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    // box-shadow: 0 0 25px #909399;
}

.login-title {
    text-align: center;
    margin: 0 auto 40px auto;
    color: #303133;
}

.el-button {
    float: right;
    margin-left: 10px;
}

.register-button {
    background: #f2f2f2;
    color: rgb(64, 158, 225);
}

.el-form-item {
    margin-left: 50px;
    margin-right: 60px;
}

.el-item_error {
    display: block !important;
}
</style>
