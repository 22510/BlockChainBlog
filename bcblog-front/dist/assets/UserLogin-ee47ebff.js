import{_ as C}from"./BClogo-c299679b.js";import{g as B,h as w,i as y,r,o as I,c as F,a as m,b as s,w as a,e as d,j as L,E as p,p as S,f as U,l as M,_ as N}from"./index-8b895a85.js";const R=n=>(S("data-v-5094454a"),n=n(),U(),n),T={class:"shell"},q={class:"BCLogo",style:{"padding-top":"5%"}},$=R(()=>m("img",{style:{width:"150px"},src:C},null,-1)),j={style:{"font-size":"60px","text-shadow":"0 0 0.5em #38a1ff, 0 0 0.2em #5c5c5c"},class:"BCName"},z=B({__name:"UserLogin",setup(n){const _=M;w("default");const f=w(),o=y({email:"",password:""}),b=y({email:[{required:!0,message:"请输入邮箱",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]}),h=()=>{p({message:"用户登录成功, 正在跳转.",type:"success"}),setTimeout(()=>{_.push("/userpage")},3e3)},v=l=>{p({message:"登录失败, "+l,type:"error"})},x=async l=>{if(!l)return console.error("错误");await l.validate((t,c)=>{t?L.post("bc208/login",{email:o.email,password:o.password}).then(function(e){e.data.success==!0&&e.data.data!=null?(localStorage.setItem("token",e.data.data),h()):(v(e.data.errorMsg),l.resetFields())}).catch(function(e){console.log(e)}):console.log("error submit!",c)})},k=()=>{p({message:"正在前往注册页面.",type:"success"}),setTimeout(()=>{_.push("/register")},2e3)};return(l,t)=>{const c=r("router-link"),e=r("el-input"),u=r("el-form-item"),g=r("el-button"),V=r("el-form");return I(),F("div",T,[m("div",q,[$,m("div",j,[s(c,{to:"/",class:"toIndex"},{default:a(()=>[d("BlockChain Studio 208")]),_:1})])]),s(V,{ref_key:"ruleFormRef",ref:f,model:o,rules:b,"label-width":"150px",class:"applicationForm","status-icon":"","label-position":"left"},{default:a(()=>[s(u,{label:"邮箱",prop:"email"},{default:a(()=>[s(e,{modelValue:o.email,"onUpdate:modelValue":t[0]||(t[0]=i=>o.email=i)},null,8,["modelValue"])]),_:1}),s(u,{label:"密码:",prop:"password"},{default:a(()=>[s(e,{modelValue:o.password,"onUpdate:modelValue":t[1]||(t[1]=i=>o.password=i)},null,8,["modelValue"])]),_:1}),s(u,{class:"submitButton"},{default:a(()=>[s(g,{type:"primary",onClick:t[2]||(t[2]=i=>x(f.value))},{default:a(()=>[d(" 登录 ")]),_:1}),s(g,{type:"primary",onClick:k},{default:a(()=>[d(" 注册 ")]),_:1})]),_:1})]),_:1},8,["model","rules"])])}}});const D=N(z,[["__scopeId","data-v-5094454a"]]);export{D as default};
