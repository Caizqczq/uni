<template>
  <div class="profile-container">
    <el-card class="profile-card" header="My Profile">
      <div v-if="authStore.loading && !profileDataLoaded" class="loading-section">
        <el-skeleton :rows="5" animated />
      </div>
      <div v-else-if="authStore.user">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Username">{{ authStore.user.username }}</el-descriptions-item>
          <el-descriptions-item label="Email">{{ authStore.user.email }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">Edit Profile</el-divider>

        <el-form ref="profileFormRef" :model="profileForm" :rules="profileRules" label-width="120px" @submit.prevent="handleUpdateProfile">
          <el-form-item label="Nickname" prop="nickname">
            <el-input v-model="profileForm.nickname" placeholder="Enter your nickname" />
          </el-form-item>

          <el-form-item label="Avatar URL" prop="avatarUrl">
            <el-input v-model="profileForm.avatarUrl" placeholder="Enter URL for your avatar" />
          </el-form-item>
          <el-form-item v-if="profileForm.avatarUrl || authStore.user.avatarUrl" label="Current Avatar">
             <el-avatar :size="60" :src="profileForm.avatarUrl || authStore.user.avatarUrl">
                <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/> <!-- Default if src fails -->
            </el-avatar>
          </el-form-item>

          <el-form-item label="School" prop="school">
            <el-input v-model="profileForm.school" placeholder="Enter your school name" />
          </el-form-item>

          <el-form-item label="Student ID" prop="studentId">
            <el-input v-model="profileForm.studentId" placeholder="Enter your student ID" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleUpdateProfile" :loading="authStore.loading">
              Save Changes
            </el-button>
            <el-button @click="resetForm">Reset</el-button>
          </el-form-item>
           <div v-if="authStore.error" class="error-message">
             <el-alert :title="authStore.error" type="error" show-icon :closable="false" />
           </div>
        </el-form>
      </div>
      <div v-else>
        <el-empty description="Could not load user profile. Please try logging in again." />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch, computed } from 'vue';
import { ElForm, ElFormItem, ElInput, ElButton, ElCard, ElMessage, ElAlert, ElDescriptions, ElDescriptionsItem, ElDivider, ElAvatar, ElSkeleton, ElEmpty } from 'element-plus';
import { useAuthStore } from '../store/auth';

const authStore = useAuthStore();
const profileFormRef = ref<InstanceType<typeof ElForm>>();
const profileDataLoaded = ref(false);

// Initialize form with empty or default values, then populate from store
const profileForm = reactive({
  nickname: '',
  avatarUrl: '',
  school: '',
  studentId: '',
});

// Computed property to get the current user from the store for display purposes (non-editable fields)
// const currentUser = computed(() => authStore.user);

// Rules for the form (optional, can be expanded)
const profileRules = reactive({
  nickname: [{ min: 2, max: 50, message: 'Nickname length should be 2 to 50', trigger: 'blur' }],
  // avatarUrl: [{ type: 'url', message: 'Please input a valid URL', trigger: 'blur' }], // Basic URL validation
});

// Function to populate form when user data is available/changes
const populateForm = () => {
  if (authStore.user) {
    profileForm.nickname = authStore.user.nickname || '';
    profileForm.avatarUrl = authStore.user.avatarUrl || '';
    profileForm.school = authStore.user.school || '';
    profileForm.studentId = authStore.user.studentId || '';
    profileDataLoaded.value = true;
  }
};

onMounted(async () => {
  // Clear any previous errors from other pages
  authStore.error = null;
  if (!authStore.user || Object.keys(authStore.user).length === 0) { // If user data is minimal or not present
    await authStore.fetchUserProfile();
  }
  populateForm(); // Populate form after fetching or if user data already exists
});

// Watch for changes in authStore.user (e.g., after fetchUserProfile completes)
watch(() => authStore.user, (newUser) => {
  if (newUser) {
    populateForm();
  }
}, { deep: true });

const handleUpdateProfile = async () => {
  if (!profileFormRef.value) return;
  profileFormRef.value.validate(async (valid) => {
    if (valid) {
      const success = await authStore.updateUserProfile({
        nickname: profileForm.nickname,
        avatarUrl: profileForm.avatarUrl,
        school: profileForm.school,
        studentId: profileForm.studentId,
      });
      if (success) {
        ElMessage.success('Profile updated successfully!');
      } else if(authStore.error) {
        // Error message is displayed via template
        // ElMessage.error(authStore.error);
      }
    } else {
      ElMessage.error('Please correct the errors in the form.');
      return false;
    }
  });
};

const resetForm = () => {
  populateForm(); // Reset to current store values
  authStore.error = null; // Clear any errors
};

</script>

<style scoped>
.profile-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 60px); /* Assuming header height is 60px */
}

.profile-card {
  width: 100%;
  max-width: 700px;
}

.el-form-item {
  margin-bottom: 22px;
}

.loading-section {
  padding: 20px;
}
.error-message {
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>
