import React, { Component } from 'react';
import api from "./services/api";
import {Radio} from "antd";

let optVector = []

class Research extends Component{
    state = {
        pages: "",
        pageIndex: 0,
        questions: [],
        answers: [],
        isLoaded: false,
        answeredPage: [],
    }
    

    componentDidMount(){
        this.getResearchData()
    }

    async getResearchData(){
        const questions = []
        const answers  = []
        const response = await api.get("/api/dados/estrutura-basica")
        // console.log(response)
        
        response.data.perguntasERespostas.map((item, i ) => {
            questions.push(item.pergunta)
            answers.push(item.respostas)
        })
        this.setState( {pages: response.data.folhas,
                        questions: questions,
                        answers: answers, 
                        isLoaded: true,} )
        // console.log(this.state)
        // console.log(this.state.pages)
        // console.log(questions)
        // console.log(answers)
    }

    handleAnswer = (e) => {
        console.log(e)
    }

    fillAnsweredPage = (questao, opcao) => {
        let obj = {}
        if (optVector.length === 0){
            obj = {"folha": this.state.pages[this.state.pageIndex].numero, 
                    "opcaoResposta": opcao, 
                    "pergunta": this.state.questions[questao].numero, 
                    "pesquisa": this.state.pages[this.state.pageIndex].pesquisa.id}
            optVector.push(obj)
        }else{
            
            optVector.map((item, id) => {
                if (this.state.questions[questao].numero == item.pergunta){
                    let remove = optVector.splice(id, 1)
                }
            })
            
            obj = {"folha": this.state.pages[this.state.pageIndex].numero, 
                "opcaoResposta": opcao, 
                "pergunta": this.state.questions[questao].numero, 
                "pesquisa": this.state.pages[this.state.pageIndex].pesquisa.id}
            optVector.push(obj)

        }
        
        // console.log(optVector)
    }

    showAnswers = (id) => {
        let answersObj = this.state.answers[id]
        // console.log(answersObj)
        return (
            <div>
                <Radio.Group >
                    {answersObj.map((options, idA) => {
                        return (
                        <Radio value = {options.opcao} 
                        onChange = {() => this.fillAnsweredPage(id, options.opcao)}>
                            {options.descricao}
                        </Radio>
                    
                    )})}
                </Radio.Group>
            </div>
        )
    }

    showQuestions = () => {
        // console.log(this.state.questions)
        return this.state.questions.map((questionObj, id) => {
            return (
                <div>
                    <h4>Pergunta {questionObj.numero}</h4>
                    <text>
                        {questionObj.descricao}:
                    </text>
                    {this.showAnswers(id)}
                </div>
            )
        })
    }

    mapPages = () => {
        if (this.state.isLoaded){
            // console.log(this.state.pages[this.state.pageIndex])
            let pageObj =  this.state.pages[this.state.pageIndex]
            return (
                <div>
                    <h2>Página {pageObj.numero}</h2>
                    {this.showQuestions()}
                    
                </div>
            )
        }
    }

    changePageIndex = (sum) => {
        let index = this.state.pageIndex + sum
        // console.log(index)
        this.setState({pageIndex: index})
    }

    handleSubmit = () => {
        this.setState({answeredPage: optVector}, () => {
            let response = api.post("/api/dados/enviar-entrevistados", this.state.answeredPage)
            console.log(response)
        })
    }

    render(){

        return(
            <div className = "research">
                <h1> Pesquisa </h1>
                
                    {
                        this.mapPages() 
                    }
                    <br/>
                    <button onClick = {this.handleSubmit}>Enviar</button>
                    <br/>
                    {
                        this.state.pageIndex === 0 ? 
                            <button onClick = {() => this.changePageIndex(1)}>Página seguinte</button>
                        : this.state.pageIndex === 1 ? 
                        <div>
                            <button onClick = {() => this.changePageIndex(-1)}>Página anterior</button>
                            <button onClick = {() => this.changePageIndex(1)}>Página seguinte</button>
                        </div>
                        :   <button onClick = {() => this.changePageIndex(-1)}>Página anterior</button>

                    }
                    
    
            </div>
        )
    }
}

export default Research