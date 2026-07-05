<script>
export default {
  name: "DateUtils",
  props: ["s"],
  data() {
    return {
      time: {
        hour: "",
        minute: "",
        second: ""
      },
      nowTime: "",
      timer: null,
      week: [
        "星期天",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六"
      ]
    };
  },
  mounted() {
    this.dataTime();
  },
  beforeDestroy() {
    clearTimeout(this.timer);
  },
  methods: {
    dataTime() {
      this.timeFormate();
      this.timer = setTimeout(() => {
        this.dataTime();
      }, 1000);
    },
    timeFormate() {
      const newTime = new Date();
      this.time.hour = this.getIncrease(newTime.getHours(), 2);
      this.time.minute = this.getIncrease(newTime.getMinutes(), 2);
      this.time.second = this.getIncrease(newTime.getSeconds(), 2);
      this.nowTime =
        this.getIncrease(newTime.getFullYear(), 4) + "年" +
        this.getIncrease(newTime.getMonth() + 1, 2) + "月" +
        this.getIncrease(newTime.getDate(), 2) + "日 " +
        this.week[newTime.getDay()];
    },
    getIncrease(num, digit) {
      var increase = "";
      for (var i = 0; i < digit; i++) {
        increase += "0";
      }
      return (increase + num).slice(-digit);
    }
  }
};
</script>

<template>
  <div>
    <span class="time" id="time">
      现在是：<span class="date">{{ nowTime }}</span>
      <span class="hour">{{ time.hour }}</span>
      <a class="split">:</a>
      <span class="minute">{{ time.minute }}</span>
      <a class="split">:</a>
      <span class="second">{{ time.second }}</span>
    </span>
  </div>
</template>

<style scoped>
.time {
  font-size: 30px;
  color: #303133;
  margin-left: 22rem;
}

.date,
.hour,
.minute,
.second {
  font-weight: 600;
}

.split{
  animation: shark 1s step-end infinite;
  vertical-align: center;
  margin-left: 2px;
  margin-right: 2px;
}
@keyframes shark{
  0%,
  100%{
    opacity: 1;
  }
  50%{
    opacity: 0;
  }
}
</style>
