/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;

import java.io.IOException;

/**
 * Created by vsubbotin on 26/05/16.
 */
public class ClipDataImpl extends SequenceBase implements ClipData {
    private static final int _TAG_EXT_SS_STATUS = 1;
    private static final int _TAG_OVERRIDE_CATEGORY = 2;
    private static final int _TAG_NOTIFICATION_TO_CSE = 3;

    private ExtSSStatus ssStatus;
    private OverrideCategory overrideCategory;
    private boolean notificationToCSE;

    public ClipDataImpl() {
        super("ClipData");
    }

    public ClipDataImpl(ExtSSStatus ssStatus, OverrideCategory overrideCategory, boolean notificationToCSE) {
        super("ClipData");
        this.ssStatus = ssStatus;
        this.overrideCategory = overrideCategory;
        this.notificationToCSE = notificationToCSE;
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    public OverrideCategory getOverrideCategory() {
        return this.overrideCategory;
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.ssStatus = null;
        this.overrideCategory = null;
        this.notificationToCSE = false;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_EXT_SS_STATUS:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter ssStatus is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.ssStatus = new ExtSSStatusImpl();
                        ((ExtSSStatusImpl)this.ssStatus).decodeAll(ais);
                        break;
                    case _TAG_OVERRIDE_CATEGORY:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter overrideCategory is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        int code = (int) ais.readInteger();
                        this.overrideCategory = OverrideCategory.getInstance(code);
                        break;
                    case _TAG_NOTIFICATION_TO_CSE:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter notificationToCSE is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        ais.readNull();
                        this.notificationToCSE = Boolean.TRUE;
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.ssStatus == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + "ssStatus is mandatory but it is absent",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.overrideCategory == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + "overrideCategory is mandatory but it is absent",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.ssStatus == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter ssStatus is not defined");
        }

        if (this.overrideCategory == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter overrideCategory is not defined");
        }

        ((ExtSSStatusImpl)this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXT_SS_STATUS);

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_OVERRIDE_CATEGORY, this.overrideCategory.getCode());

            if (this.notificationToCSE) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NOTIFICATION_TO_CSE);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus);
        }
        if (this.overrideCategory != null) {
            sb.append(", overrideCategory=");
            sb.append(this.overrideCategory);
        }
        if (this.notificationToCSE) {
            sb.append(", notificationToCSE");
        }

        sb.append("]");
        return sb.toString();
    }
}
