<template>
  <div class="register-container">
    <el-card class="register-card" header="Create UniLife Account">
      <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" @submit.prevent="handleRegister">
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="Username"
            :prefix-icon="UserIcon"
          />
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="Email Address"
            :prefix-icon="MessageIcon"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="Password"
            show-password
            :prefix-icon="LockIcon"
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="Confirm Password"
            show-password
            :prefix-icon="LockIcon"
          />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="Nickname (Optional)"
            :prefix-icon="UserFilledIcon"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="register-button"
            @click="handleRegister"
            :loading="authStore.loading"
          >
            Register
          </el-button>
        </el-form-item>
        <div v-if="authStore.error" class="error-message">
          <el-alert :title="authStore.error" type="error" show-icon :closable="false" />
        </div>
         <div v-if="successMessage" class="success-message">
          <el-alert :title="successMessage" type="success" show-icon :closable="false" />
        </div>
        <el-form-item>
          <div class="form-links">
            <router-link to="/login">Already have an account? Login</router-link>
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
import { User as UserIcon, Lock as LockIcon, Message as MessageIcon, UserFilled as UserFilledIcon } from '@element-plus/icons-vue';
import { useAuthStore } from '../store/auth';

const router = useRouter();
const authStore = useAuthStore();

const registerFormRef = ref<InstanceType<typeof ElForm>>();
const successMessage = ref('');

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  nickname: '',
});

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Please input the password again'));
  } else if (value !== registerForm.password) {
    callback(new Error("Passwords don't match!"));
  } else {
    callback();
  }
};

const registerRules = reactive({
  username: [
    { required: true, message: 'Please input username', trigger: 'blur' },
    { min: 4, max: 20, message: 'Length should be 4 to 20', trigger: 'blur' },
  ],
  email: [
    { required: true, message: 'Please input email address', trigger: 'blur' },
    { type: 'email', message: 'Please input correct email address', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: 'Please input password', trigger: 'blur' },
    { min: 6, max: 20, message: 'Length should be 6 to 20', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: 'Please confirm password', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' },
  ],
  nickname: [], // Optional
});

const handleRegister = async () => {
  if (!registerFormRef.value) return;
  successMessage.value = ''; // Clear previous success message
  authStore.error = null; // Clear previous error

  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      await authStore.register({
        username: registerForm.username,
        email: registerForm.email,
        password: registerForm.password,
        nickname: registerForm.nickname,
      });
      if (authStore.error) {
        // Error message displayed via template
      } else {
        successMessage.value = 'Registration successful! Please check your email for verification and then login.';
        ElMessage.success('Registration successful! Please verify your email.');
        // Optionally redirect to login or a page indicating to check email
        // router.push('/login');
        registerFormRef.value?.resetFields(); // Clear form
      }
    } else {
      ElMessage.error('Please correct the errors in the form.');
      return false;
    }
  });
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  background-color: #f0f2f5;
}

.register-card {
  width: 450px; /* Slightly wider for more fields */
  padding: 20px;
}

.register-button {
  width: 100%;
}

.form-links {
  display: flex;
  justify-content: center; /* Center link for register page */
  width: 100%;
  margin-top: 10px;
}

.form-links a {
  font-size: 0.9em;
  color: #409EFF;
  text-decoration: none;
}

.form-links a:hover {
  text-decoration: underline;
}

.error-message, .success-message {
  margin-bottom: 15px;
}
</style>
