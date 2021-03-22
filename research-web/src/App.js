import './App.css';
import React, { Component } from 'react';
import api from "./services/api";
import { isAuthenticated, login } from "./services/auth";
import { withRouter } from "react-router-dom";



class App extends Component{
  state = {
    email: "",
    password: "",
    errors:{},
    postResponse: false,
  }

  componentDidMount(){
    console.log(isAuthenticated())
    if (isAuthenticated()){
      this.props.history.push("/dashboard");
    }else{
      this.props.history.push("/");
    }
  }

  handleValidation = () => {
    let email = this.state.email;
    let password = this.state.password;
    let errors = {};
    let formIsValid = true;


    //Email
    if(!email){
      formIsValid = false;
      errors["email"] = "Email não pode ser vazio";
    }

    if(typeof email !== "undefined"){
      let lastAtPos = email.lastIndexOf('@');
      let lastDotPos = email.lastIndexOf('.');

      if (!(lastAtPos < lastDotPos && 
        lastAtPos > 0 && 
        email.indexOf('@@') === -1 && 
        lastDotPos > 2 && 
        (email.length - lastDotPos) > 2)) {
        
        formIsValid = false;
        errors["email"] = "Email não válido";
      }
    }

    //password
    if(!password){
      formIsValid = false;
      errors["password"] = "Senha não pode ser vazia";
    }

    this.setState({errors: errors});
    return formIsValid;
  }

  initDB = async () => {
    const response = await api.post("/api/dados/initDB")
    this.setState({isInitiated: true})
    console.log(response)
  }

  handleSign = async e => {
    e.preventDefault();
    const { email, password, errors } = this.state;
    if (!this.handleValidation()) {
      if (errors["email"]){
        this.setState({ postResponse: errors["email"] });
      }else if(errors["password"]){
        this.setState({ postResponse: errors["password"] });
      }else{
        this.setState({ postResponse: "Preencha todos os dados para se cadastrar" });
      }
    } else {
      try {
        const response = await api.post("/login", { email, password });
        this.setState({ postResponse: response.status });
        login(response.data.token)
        this.initDB()
        this.props.history.push("/dashboard");
      } catch (err) {
        console.log(err);
        this.setState({ postResponse: "Ocorreu um erro ao registrar ou logar sua conta. T.T" });
      }
    }
  };

  render(){
    const { postResponse } = this.state;

    return(
      <div className = "App">
        <h1>Login</h1>
        <form name="loginform" className="loginform" onSubmit= {this.handleSign}>
          <input name= "email" type="email" className="logininput"
          placeholder="Email" 
          onChange={e => this.setState({ email: e.target.value })} />
          
          <br/>
          <input name= "password" type="password" className="logininput"
          placeholder="Senha" 
          onChange={e => this.setState({ password: e.target.value })}/>
          
          <br/>
          <button type="submit" className="loginbutton">Entrar</button>
          
          <br/>
        </form>
      </div>
    )
  }
}

export default withRouter(App);
