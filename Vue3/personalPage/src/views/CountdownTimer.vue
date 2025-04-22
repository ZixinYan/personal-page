<template>
  <el-row class="timer-page">
    <el-col :span="24" class="timer-container" :style="{ backgroundImage: 'url(' + background + ')' }">
      <div class="timer">
        <h1>{{ minutes }}:{{ seconds < 10 ? '0' : '' }}{{ seconds }}</h1>
        <div class="buttons">
          <el-button type="primary" v-if="!isActive" @click="startTimer">开始倒计时</el-button>
          <el-button type="info" v-if="isActive" @click="endTimer">结束倒计时</el-button>
          <el-button type="danger" @click="resetLockState">强制解锁</el-button> <!-- 强制解锁按钮 -->
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { scheduleUpdateService } from '@/api/schedule.js';
import CryptoJS from 'crypto-js';

// 获取路由实例
const router = useRouter();
const route = useRoute();
const row = ref(null); // 使用 ref 声明响应式变量

// 解密函数
const decryptRow = (encryptedRow, secretKey) => {
  const bytes = CryptoJS.AES.decrypt(encryptedRow, secretKey);
  const decryptedData = bytes.toString(CryptoJS.enc.Utf8);
  return JSON.parse(decryptedData);
};

// 从路由查询参数获取加密的数据
const encryptedRow = route.query.row;

if (encryptedRow) {
  const secretKey = 'helloworld'; // 确保与加密时使用的密钥相同
  row.value = decryptRow(encryptedRow, secretKey); // 赋值给响应式变量
  console.log(row.value); // 现在可以使用 row 变量中的解密数据
} else {
  console.log('没有提供数据');
}

// 25 分钟倒计时
const totalTime = ref(25*60);
const timer = ref(null);
const isActive = ref(false);

// 计算分钟和秒钟
const minutes = computed(() => Math.floor(totalTime.value / 60));
const seconds = computed(() => totalTime.value % 60);

// 随机背景图片
const background = ref('');

onMounted(async () => {
  const images = import.meta.glob('@/assets/login/*.png');
  const keys = Object.keys(images);
  const randomKey = keys[Math.floor(Math.random() * keys.length)];
  background.value = await images[randomKey]().then(mod => mod.default);
});

const scheduleModel = ref({
  id: '',
  content: '',
  finish: '',
  priority: 0,
  predictTime: '',
  actualTime: '',
  rawDeadline: ''
});

const lockInfo = ref({ count: 0, unlockTime: 0, lastLockTime: Date.now() }); // 添加 lastLockTime

const isButtonLocked = () => {
  if (lockInfo.value.unlockTime > Date.now()) {
    return true;
  }
  return false;
};

const lockButton = () => {
  const currentTime = Date.now();

  // 检查上次锁定时间与当前时间的间隔
  if (currentTime - lockInfo.value.lastLockTime > 60 * 60 * 1000) {
    lockInfo.value.count = 0; // 超过1小时，重置计数
  }

  if (lockInfo.value.count !== 3) {
    lockInfo.value.count++;
    lockInfo.value.unlockTime = currentTime + 3 * 60 * 1000; // 锁定3分钟
  } else {
    lockInfo.value.count = 0;
    lockInfo.value.unlockTime = currentTime + 30 * 60 * 1000; // 锁定30分钟
  }

  lockInfo.value.lastLockTime = currentTime; // 更新最后锁定时间
  localStorage.setItem('buttonLock', JSON.stringify(lockInfo.value));
};

onMounted(() => {
  const storedLockInfo = localStorage.getItem('buttonLock');
  if (storedLockInfo) {
    lockInfo.value = JSON.parse(storedLockInfo);
  }

  // 检查锁定状态
  if (isButtonLocked()) {
    const remainingTime = Math.ceil((lockInfo.value.unlockTime - Date.now()) / 1000);
    ElMessage.warning(`倒计时按钮已锁定，将在${Math.ceil(remainingTime / 60)}分钟后解锁`);
  }
});

// 强制解锁的方法
const resetLockState = () => {
  lockInfo.value.unlockTime = 0; // 取消锁定时间
  lockInfo.value.lastLockTime = Date.now(); // 更新最后锁定时间为当前时间
  localStorage.setItem('buttonLock', JSON.stringify(lockInfo.value)); // 更新本地存储
  ElMessage.success('锁定状态已重置！'); // 提示用户
};

const startTimer = () => {
  if (!isActive.value && !isButtonLocked()) {
    isActive.value = true;
    timer.value = setInterval(() => {
      if (totalTime.value > 0) {
        totalTime.value--;
      } else {
        clearInterval(timer.value);
        isActive.value = false;
        ElMessage.success('你已经完成了一个番茄钟了喵！该休息了喵');
        totalTime.value = 25 * 60; // 重置时间

        // 调用更新函数
        updatescheduleState(row.value); // 传递响应式变量的值
        lockButton(); // 在倒计时结束后锁定按钮
        router.push('/schedule/manage');
      }
    }, 1000);
  }
};

// 结束倒计时并重置
const endTimer = () => {
  clearInterval(timer.value);
  isActive.value = false;
  totalTime.value = 25 * 60; // 重置时间
  ElMessage.info('倒计时已结束');
};

// 更新日程状态
const updatescheduleState = async (row) => {
  if (!row.actualTime) row.actualTime = 0;
  scheduleModel.value = { ...row, actualTime: row.actualTime + 1 };
  await scheduleUpdateService(scheduleModel.value);
};
</script>

<style scoped>
.timer-page {
  height: 100vh;
  background-color: #b1d9e8;
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(10px);
  padding: 20px;
}

.timer-container {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.5);
  padding: 20px;
}

.timer {
  width: 30%;
  background: rgba(0, 0, 0, 0.7);
  padding: 60px;
  border-radius: 20px;
  text-align: center;
  color: white;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.5);
  user-select: none; 
}

.timer h1 {
  font-size: 120px;
  margin: 0;
}

.buttons {
  margin-top: 20px;
}

.buttons .el-button {
  font-size: 20px;
  padding: 15px 30px;
  margin: 40px 70px;
}
</style>
