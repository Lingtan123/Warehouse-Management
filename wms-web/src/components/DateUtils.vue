<script>
  export default {
    name: "DateUtils",
    props:["s"],
    data() {
      return {
        time:{
          hour: "",
          minute: "",
          second: "",
        },
        nowTime:"",
        week:[
          "星期天",
          "星期一",
          "星期二",
          "星期三",
          "星期四",
          "星期五",
          "星期六",
        ]
      }
    },
    mounted() {
      this.dataTime();
    },
    methods: {
      dataTime(){
        this.timeFormate();
        setTimeout(()=>{
          this.dataTime();
        },1000);
      },
      timeFormate(){
        const newTime = new Date();
        this.time.hour = this.getIncrease(newTime.getHours(),2);
        this.time.minute = this.getIncrease(newTime.getMinutes(),2);
        this.time.second = this.getIncrease(newTime.getSeconds(),2);
        this.nowTime =
            this.getIncrease(newTime.getFullYear(),4)+"年"+
            this.getIncrease(newTime.getMonth()+1,2)+"月"+
            this.getIncrease(newTime.getDate(),2)+"日"+
            this.week;
      },
      getIncrease(num,digit){
        var increase="";
        for(var i=0;i<digit;i++){
          increase+="0";
        }
        return (increase + num).slice(-digit);
      }
    }
  }
</script>

<template>
  <div style="">
    <span class="time" id="time">
      今天是:<span class="date">{{ nowTime }}</span>
      <span class="hour">{{time.hour}}</span>
      <a class="split">:</a>
      <span class="minute">{{time.minute}}</span>
      <a class="split">:</a>
      <span class="second">{{time.second}}</span>
    </span>
  </div>
</template>

<style scoped>
.txt-data .time{
  fone-size: 1rem;
  margin-right: 0.5rem;
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