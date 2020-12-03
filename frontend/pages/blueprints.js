import React, {useEffect, useState} from "react";
import axios from 'axios'
import {Button} from "react-bootstrap";
import BlueprintCard from "../components/cards/BlueprintCard";

export default function Blueprints() {

    const [blueprints, setBlueprints] = useState([])
    const [next, setNext] = useState("/api/v1/blueprint?amount=6")

    const getNextPage = () => {
        axios
            .get(next)
            .then(res => {
                setBlueprints([...blueprints, ...res.data.data.list])
                if (res.data.data.next) setNext(res.data.data.next)
                else setNext(null)
            })
    }

    useEffect(() => {
        if (next) getNextPage()
    }, []);

    return (
        <div>
            <h1>Blueprints</h1>
            <div className={"d-flex flex-wrap"}>
                {blueprints.map(bp => <BlueprintCard blueprint={bp}/>)}
            </div>
            {next && <Button className={"m-1"} onClick={getNextPage}>Get more</Button>}
        </div>
    )

};