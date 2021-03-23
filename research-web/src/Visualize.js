import React, { Component } from 'react';
import {Table} from "antd";
import "./Visualize.css"

const columns = [
    {
        title: "Folha",
        dataIndex: "folha",
        key : "folha",
    },
    {
        title: "Opção da Resposta",
        dataIndex: "opcaoResposta",
        key : "opcaoResposta",
    },
    {
        title: "Pergunta",
        dataIndex: "pergunta",
        key : "pergunta",
    },
    {
        title: "Pesquisa",
        dataIndex: "pesquisa",
        key : "pesquisa",
    },
    
]


class Visualize extends Component{
    state = {
        loadedPages: [],
    }

    //Carrega as pesquisas efetuadas
    componentDidMount(){
        const response = JSON.parse(localStorage.getItem("tempResearch1"))
        const response2 = JSON.parse(localStorage.getItem("tempResearch2"))
        const response3 = JSON.parse(localStorage.getItem("tempResearch3"))
        const data = []
        if (response){
            data.push(response)
        }if (response2){
            data.push(response2)
        }if (response3){
            data.push(response3)
        }
        this.setState({loadedPages: data})
    }

    //Recebe os dados para o preenchimento da tabela
    getData = () => {
        const data = []
        this.state.loadedPages.map((item, id) => {
            item.map((element) => {
                data.push(element)
            })
        })
        return data
    }
    
    //Volta para o dashboar
    handleBack = () => {
        this.props.history.goBack()
    }

    render(){
        return(
            <div className = "Visualize">
                <button className = "Back"onClick={this.handleBack}>Voltar</button>
                <h1 className = "Title">Visualização de respostas</h1>
                <Table columns = {columns} dataSource = {this.getData()}></Table>
            </div>
        )
    }
}

export default Visualize;