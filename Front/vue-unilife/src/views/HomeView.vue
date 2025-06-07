<template>
  <div>
    <h1>Home Page - Welcome!</h1>
    <p v-if="authStore.user">Welcome, {{ authStore.user.nickname || authStore.user.username }}!</p>
    <el-button @click="handleLogout">Logout</el-button>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from "../store/auth";
import { ElButton } from 'element-plus';

const authStore = useAuthStore();
const handleLogout = () => {
  authStore.logout();
};
</script>

<style scoped>
/* Add any specific styles for HomeView here */
div {
  text-align: center;
  margin-top: 50px;
}
</style>
