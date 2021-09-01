import React, {useState} from 'react';
import {useDispatch} from 'react-redux';

import * as actions from '../actions';

const FindAllCompanies = () => {

    const dispatch = useDispatch();
    const [keywords, setKeywords] = useState('');

    const handleSubmit = event => {
        event.preventDefault();

        dispatch(actions.findAllCompanies(
            {keywords: keywords.trim(), 
            page: 0}));
    }

    return (

        <form className="form-inline justify-content-center" onSubmit={e => handleSubmit(e)}>
            <div className="card">
                <article className="card-body">
                    <input id="keywords" placeholder="Nombre del restaurante" type="text" className="form-control mr-sm-2"
                        value={keywords} onChange={e => setKeywords(e.target.value)}/>
                    <button type="submit" className="btn btn-primary my-2 my-sm-0">
                        Buscar
                    </button>
                </article>
            </div>
        </form>

    );

}

export default FindAllCompanies;
