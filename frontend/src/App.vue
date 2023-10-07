<template>
    <div id="app">
        <nav class="nav_bg" v-show="localpath" v-if="isRouterAlive">
            <div class="left" id="dynamic_nav">
                <a href="/" :class="{ active: $route.path === '/' }">首页</a>
                <a href="/site" :class="{ active: $route.path === '/site' }">景点</a>
                <a href="/flow" :class="{ active: $route.path === '/flow' }">发现</a>
                <a href="/search" :class="{ active: $route.path === '/serach' }">文章搜索</a>
                <a href="/im" :class="{ active: $route.path === '/im' }">聊天</a>
            </div>
            <div class="right">
                <span style="margin-right: 30px;" v-show="$store.state.id !== ''" @click="isRead()">
                    <el-badge :is-dot="unreadCount !== 0" class="item">
                        <i class="fa-regular fa-bell fa-xl"></i>
                    </el-badge> {{ unreadCount }}条未读消息
                </span>
                <el-popover placement="top-start" title="" width="60" trigger="hover" content="">
                    <div class="personal-wrapper" v-if="this.$store.state.name !== ''">
                        <div class="personal-item" @click="login()">个人主页</div>
                        <div class="personal-item" @click="createCenter()">创作中心</div>
                        <div class="personal-item" @click="logout()">退出账号</div>
                    </div>
                    <a @click="login()" slot="reference" cursor="pointer">
                        <img v-if="this.$store.state.avatar" class="login-icon" :src="this.$store.state.avatar"
                            style="border: 5px solid RGB(144, 238, 144);" />
                        <img v-else class="login-icon" src="./assets/img/img.jpg" />
                        <el-avatar v-else class="login-icon" icon="el-icon-user-solid"></el-avatar>
                    </a>
                </el-popover>
            </div>
        </nav>
        <AdminHome v-show="localpath1"></AdminHome>
        <router-view style="padding-top: 55px" v-if="isRouterAlive" :key="key" />
    </div>
</template>


<style src="./assets/css/theme/white.css"></style>
<style src="./assets/css/reset.css"></style>
<style src="./assets/css/nav/nav.css"></style>
<script>
import adminhome from "../src/views/backend/AdminHome.vue"
import { logout } from "@/api/login"
import { getMyUnreadCount, allRead } from "@/api/notification"
const ele = require('element-ui');
export default {
    name: 'vue_main',
    components: {
        AdminHome: adminhome
    },
    provide() {
        return {
            reload: this.reload
        };
    },
    mounted() {
        this.userName = this.$store.state.name;
        setTimeout(() => {
            this.getUnreadCount();
        }, 200);
    },
    watch: {
        $route(to) {
                let path = to.path;
                if (path.indexOf("admin") != -1) {
                    if (path == "/admin/login") {
                        this.localpath = false;
                        this.localpath1 = false;
                    }
                    else {
                        this.localpath = false;
                        this.localpath1 = true;
                    }
                }
                else {
                    this.localpath = true;
                    this.localpath1 = false;
                }
            }
        },
        data() {
            return {
                token: '',
                isRouterAlive: true,
                userName: "",
                path: "",
                localpath: true,
                localpath1: false,
                unreadCount: ''
            }
        },
        methods: {
            reload() {
                this.isRouterAlive = false;
                this.$nextTick(() => {
                    this.isRouterAlive = true;
                });
            },
            login() {
                const { name, id } = this.$store.state;
                const path = name !== "" ? `/personal/${id}` : "/login";
                this.$router.push({ path });
            },
            async logout() {
                const res = await logout(this.$store.state.token);
                if (res.success) {
                    window.localStorage.removeItem('store');
                    window.localStorage.removeItem('token');
                    this.$store.commit('SET_NAME', "");
                    this.$store.commit('SET_ID', "");
                    this.$store.commit('SET_AVATAR', "");
                    this.$store.commit('SET_TOKEN', "");
                    this.$router.push('/login');
                    ele.Message.success("退出当前账号");
                }
            },
            createCenter() {
                this.$router.push("/createCenter");
            },
            async getUnreadCount() {
                const { id } = this.$store.state;
                if (id !== "") {
                    const res = await getMyUnreadCount(id);
                    this.unreadCount = res.data;
                }
            },
            isRead() {
                const { id } = this.$store.state;
                this.$router.push(`/personal/mymessage/${id}`);
                allRead(id);
                this.unreadCount = 0;
            }
        },
        created() {
            const storedData = localStorage.getItem('store');
            if (storedData && JSON.parse(storedData).id !== '') {
                this.$store.replaceState({
                    ...this.$store.state,
                    ...JSON.parse(storedData)
                });
            }
            window.addEventListener('beforeunload', () => {
                localStorage.setItem('store', JSON.stringify(this.$store.state));
            });
        },
        computed: {
            key() {
                return this.$route.path + Math.random();
            }
        }
    };

</script>
<style lang="scss">
el-menu-item {
    height: 30px;
}

.personal-wrapper {
    padding-left: 5px;
    padding-right: 5px;
    text-align: center;
}

.personal-item {
    cursor: pointer;
    line-height: 40px;
}

.personal-item :hover {
    background-color: #aaaaaa;
}

.header-logo {
    height: 50px;
    position: fixed;
    left: 10px;
    top: 5px;
    z-index: 10;
}

.login-icon {
    z-index: 1;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: solid 2px lightgray;
    cursor: pointer;
}

.header-user {
    position: fixed;
    right: 30px;
    top: 10px;
    height: 50px;
    line-height: 50px;
}

.el-popper {
    min-width: 120px;
}

#app {
    width: 100%;
}
</style>