<template>
  <div id="app">
    <FormBuilder :form="{display: 'form'}" :options="{}" @change="(schema) => console.log(schema)" /></div>
</template>
<script>
import { FormBuilder } from 'vue-formio'
export default {
  name: 'App',
  // eslint-disable-next-line vue/no-unused-components
  components: { FormBuilder }
}
</script>
<style>

</style>
