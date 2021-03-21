import React, { Component } from 'react';
import api from "./services/api";

class Dashboard extends Component
{   
    
    componentDidMount(){
        this.initDB()
    }
    
    initDB = async () => {
        const response = await api.post("/api/dados/initDB")
        console.log(response)
    }
    
    handleClick = () => {
        this.props.history.push("/research");
    }
    
    render(){
        return(
            <div>
                <h1>Dashboard</h1>
                <button onClick = {this.handleClick}>Adicionar uma entrevista </button>
                <button>Visualizar respostas</button>
            </div>
        )
    } 
}
export default Dashboard;