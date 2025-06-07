<template>
  <div class="topic-list-container">
    <el-card shadow="never" class="topic-card">
      <template #header>
        <div class="card-header">
          <span>Topics</span>
        </div>
      </template>
      <div v-if="forumStore.isLoadingTopics" class="loading-topics">
        <el-skeleton :rows="3" animated />
      </div>
      <div v-else-if="forumStore.error && forumStore.topics.length === 0" class="error-topics">
        <el-alert title="Could not load topics" type="error" :description="forumStore.error" show-icon />
      </div>
      <div v-else-if="forumStore.topics.length === 0" class="no-topics">
        <el-empty description="No topics available yet." />
      </div>
      <div v-else>
        <el-tag
          v-for="topic in forumStore.topics"
          :key="topic.id"
          :type="selectedTopicId === topic.id ? 'primary' : 'info'"
          class="topic-tag"
          effect="light"
          @click="selectTopic(topic.id)"
        >
          {{ topic.name }}
        </el-tag>
        <el-tag
          :type="selectedTopicId === null ? 'primary' : 'info'"
          class="topic-tag"
          effect="light"
          @click="selectTopic(null)"
        >
          All Posts
        </el-tag>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, defineEmits, defineProps } from 'vue';
import { useForumStore } from '../../store/forum';
import { ElCard, ElTag, ElSkeleton, ElAlert, ElEmpty } from 'element-plus';

const forumStore = useForumStore();

// Props to receive the currently selected topic ID from parent
const props = defineProps<{
  selectedTopicId: number | null;
}>();

// Emit an event when a topic is selected
const emit = defineEmits(['topic-selected']);

onMounted(() => {
  if (forumStore.topics.length === 0) { // Fetch only if not already loaded
    forumStore.fetchTopics();
  }
});

const selectTopic = (topicId: number | null) => {
  emit('topic-selected', topicId);
};
</script>

<style scoped>
.topic-list-container {
  margin-bottom: 20px;
}
.topic-card {
  border-radius: 8px;
}
.card-header {
  font-weight: bold;
}
.topic-tag {
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}
.topic-tag:hover {
  opacity: 0.8;
}
.loading-topics, .error-topics, .no-topics {
  padding: 20px;
  text-align: center;
}
</style>
