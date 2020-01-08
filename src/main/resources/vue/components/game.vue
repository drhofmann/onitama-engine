<template id="position">
      <table class="table-body">
        <tbody>
          <tr v-for="row in board">
            <td v-for="singleSquare in row">
             <div class="square">
                <div class="piece"> {{singleSquare}} </div>
             </div>
            </td>
          </tr>
        </tbody>
      </table>
</template>
<script>
    Vue.component("onitama-game",
     {template: "#position",
     data: () => ({
     board:[]
     }),
     created() {
     fetch("/api/position")
     .then(res => res.json())
     .then(res => this.board = res)
     .catch(() => alert("Error while fetching board"));
     }
     });
</script>
<style>
    .square{
    width:100px;
    height:100px;
    background:goldenrod;
    }
    .square:hover{
    background:lightblue
    }
    .piece{
    font-size:xx-large;
    text-align: center;
    vertical-align: 80px;
    }
</style>