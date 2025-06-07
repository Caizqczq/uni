<template>
  <div class="login-container">
    <el-card class="login-card" header="UniLife Login">
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" @submit.prevent="handleLogin">
        <el-form-item prop="usernameOrEmail">
          <el-input
            v-model="loginForm.usernameOrEmail"
            placeholder="Username or Email"
            :prefix-icon="UserIcon"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="Password"
            show-password
            :prefix-icon="LockIcon"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            @click="handleLogin"
            :loading="authStore.loading"
          >
            Login
          </el-button>
        </el-form-item>
        <div v-if="authStore.error" class="error-message">
          <el-alert :title="authStore.error" type="error" show-icon :closable="false" />
        </div>
        <el-form-item>
          <div class="form-links">
            <router-link to="/register">Don't have an account? Register</router-link>
            <!-- <router-link to="/forgot-password">Forgot Password?</router-link> -->
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElForm, ElFormItem, ElInput, ElButton, ElCard, ElMessage, ElAlert } from 'element-plus';
import { User as UserIcon, Lock as LockIcon } from '@element-plus/icons-vue'; // Import icons
import { useAuthStore } from '../store/auth';

const router = useRouter();
const authStore = useAuthStore();

const loginFormRef = ref<InstanceType<typeof ElForm>>();

const loginForm = reactive({
  usernameOrEmail: '',
  password: '',
});

const loginRules = reactive({
  usernameOrEmail: [{ required: true, message: 'Please input username or email', trigger: 'blur' }],
  password: [{ required: true, message: 'Please input password', trigger: 'blur' }],
});

const handleLogin = async () => {
  if (!loginFormRef.value) return;
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      await authStore.login(loginForm);
      if (authStore.isAuthenticated) {
        ElMessage.success('Login successful!');
        // Redirect is handled by the store action now
        // router.push('/home');
      } else if (authStore.error) {
        // Error message is displayed via the template
        // ElMessage.error(authStore.error); // Or use ElMessage if not using template error display
      }
    } else {
      ElMessage.error('Please fill in all required fields.');
      return false;
    }
  });
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh; /* Make it take most of the viewport height */
  background-color: #f0f2f5; /* Light grey background */
}

.login-card {
  width: 400px;
  padding: 20px;
}

.login-button {
  width: 100%;
}

.form-links {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 10px;
}

.form-links a {
  font-size: 0.9em;
  color: #409EFF; /* Element Plus primary color */
  text-decoration: none;
}

.form-links a:hover {
  text-decoration: underline;
}

.error-message {
  margin-bottom: 15px;
}
</style>
