<template id="inputmove">
<div>
    <input type="text" v-model="enteredMove" placeholder="enter move">
    <label> {{enteredMove}} </label>
</div>
</template>

<script>
    Vue.component("inputmove", {template: "#inputmove",
    data: () => ({
               enteredMove:""
               })
    }
    );
</script>