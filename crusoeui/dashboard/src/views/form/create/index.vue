<template>
  <div class="app-container">
    <form-create v-model="$data.$f" :rule="rule" @on-submit="onSubmit"></form-create>
  </div>
</template>

<script>
import FormCreate from "@form-create/element-ui";

export default {
  data() {
    return {
      //表单实例对象
      $f: {},
      //表单生成规则
      rule: [
        {
          type: "input",
          field: "goods_name",
          title: "商品名称"
        },
        {
          type: "datePicker",
          field: "created_at",
          title: "创建时间"
        }
      ]
    };
  },
  methods: {
    onSubmit(formData) {
      //TODO 提交表单
    }
  },
  components: {
    formCreate: FormCreate.$form()
  }
};
</script>
