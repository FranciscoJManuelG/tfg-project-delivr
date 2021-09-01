import React from 'react';
import {FormattedMessage} from 'react-intl';

const Footer = () => (

    <div>
        <footer className="navbar-fixed-bottom">
        <br/>
        <hr/>
        <br/>
            <p className="text-center">
                <FormattedMessage id="project.app.Footer.text"/>
            </p>
        </footer>
    </div>

);

export default Footer;
