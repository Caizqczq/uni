<template>
  <el-card class="post-form-card">
    <template #header>
      <div class="card-header">
        <span>{{ isEditMode ? 'Edit Post' : 'Create New Post' }}</span>
      </div>
    </template>
    <el-form ref="postFormRef" :model="formData" :rules="formRules" label-position="top" @submit.prevent="handleSubmit">
      <el-form-item label="Title" prop="title">
        <el-input v-model="formData.title" placeholder="Enter post title" :disabled="isLoading" />
      </el-form-item>

      <el-form-item label="Topic" prop="topicId">
        <el-select
          v-model="formData.topicId"
          placeholder="Select a topic"
          :disabled="isLoading || forumStore.isLoadingTopics"
          filterable
          class="topic-select"
        >
          <el-option
            v-for="topic in forumStore.topics"
            :key="topic.id"
            :label="topic.name"
            :value="topic.id"
          />
        </el-select>
        <div v-if="forumStore.isLoadingTopics" class="loading-text">Loading topics...</div>
      </el-form-item>

      <el-form-item label="Content" prop="content">
        <el-input
          type="textarea"
          :rows="10"
          v-model="formData.content"
          placeholder="Write your post content here... Markdown is supported."
          :disabled="isLoading"
        />
        <!-- Basic Markdown Preview (Optional) -->
        <div v-if="formData.content" class="markdown-preview-simple">
          <p><strong>Preview:</strong></p>
          <div v-html="renderMarkdown(formData.content)" class="rendered-markdown-content"></div>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="isLoading || internalLoading">
          {{ isEditMode ? 'Save Changes' : 'Create Post' }}
        </el-button>
        <el-button @click="handleCancel" :disabled="isLoading || internalLoading">Cancel</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch, defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';
import { useForumStore } from '../../store/forum';
import { ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElButton, ElCard, ElMessage } from 'element-plus';
import DOMPurify from 'dompurify';
import { marked } from 'marked';

interface PostFormData {
  title: string;
  content: string;
  topicId: number | null;
}

const props = defineProps<{
  initialData?: Partial<PostFormData>; // Partial because topicId might be null initially
  isEditMode: boolean;
  isLoading: boolean; // External loading state from parent view
}>();

const emit = defineEmits(['submit-form', 'cancel']);

const router = useRouter();
const forumStore = useForumStore();
const postFormRef = ref<InstanceType<typeof ElForm>>();
const internalLoading = ref(false); // For topic loading

const formData = reactive<PostFormData>({
  title: '',
  content: '',
  topicId: null,
});

const formRules = reactive({
  title: [
    { required: true, message: 'Please enter a title', trigger: 'blur' },
    { min: 5, max: 150, message: 'Title length should be 5 to 150 characters', trigger: 'blur' },
  ],
  topicId: [{ required: true, message: 'Please select a topic', trigger: 'change' }],
  content: [{ required: true, message: 'Please enter content for your post', trigger: 'blur' }],
});

const populateForm = (data?: Partial<PostFormData>) => {
  if (data) {
    formData.title = data.title || '';
    formData.content = data.content || '';
    formData.topicId = data.topicId || null;
  } else {
    formData.title = '';
    formData.content = '';
    formData.topicId = null;
  }
};

onMounted(async () => {
  internalLoading.value = true;
  if (forumStore.topics.length === 0) {
    try {
      await forumStore.fetchTopics();
    } catch (error) {
      ElMessage.error('Failed to load topics. Please try again.');
    }
  }
  populateForm(props.initialData);
  internalLoading.value = false;
});

watch(() => props.initialData, (newData) => {
  populateForm(newData);
}, { deep: true, immediate: true });


const handleSubmit = () => {
  postFormRef.value?.validate((valid) => {
    if (valid && formData.topicId !== null) { // Ensure topicId is not null
      emit('submit-form', { ...formData, topicId: formData.topicId });
    } else if (formData.topicId === null) {
        ElMessage.error('Please select a topic.');
    } else {
      ElMessage.error('Please correct the errors in the form.');
    }
  });
};

const handleCancel = () => {
    emit('cancel');
    // router.go(-1); // Or navigate to a specific route like forum home
};

const renderMarkdown = (content: string) => {
  if (!content) return '';
  return DOMPurify.sanitize(marked(content) as string);
};

</script>

<style scoped>
.post-form-card {
  max-width: 800px;
  margin: 20px auto;
  border-radius: 8px;
}
.card-header {
  font-size: 1.5em;
  font-weight: bold;
}
.topic-select {
  width: 100%;
}
.loading-text {
  font-size: 0.9em;
  color: #909399;
}
.markdown-preview-simple {
  margin-top: 10px;
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #f9f9f9;
  font-size: 0.9em;
}
.rendered-markdown-content :deep(p) {
  margin-bottom: 0.5em;
}
.rendered-markdown-content :deep(h1),
.rendered-markdown-content :deep(h2),
.rendered-markdown-content :deep(h3) {
  margin-top: 0.8em;
  margin-bottom: 0.3em;
}
</style>
