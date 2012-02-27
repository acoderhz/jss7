/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class DestinationRoutingAddressTest {

	public byte[] getData1() {
		return new byte[] { (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16 };
	}

	public byte[] getIntData1() {
		return new byte[] { 2, 16, 121, 34, 16 };
	}

	@Test(groups = { "functional.decode","circuitSwitchedCall.primitive"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		DestinationRoutingAddressImpl elem = new DestinationRoutingAddressImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		assertNotNull(elem.getCalledPartyNumber());
		assertEquals(elem.getCalledPartyNumber().size(), 1);
		assertTrue(Arrays.equals(elem.getCalledPartyNumber().get(0).getData(), this.getIntData1()));
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall.primitive"})
	public void testEncode() throws Exception {

		ArrayList<CalledPartyNumberCap> cpnl = new ArrayList<CalledPartyNumberCap>();
		CalledPartyNumberCapImpl cpn = new CalledPartyNumberCapImpl(getIntData1());
		cpnl.add(cpn);
		DestinationRoutingAddressImpl elem = new DestinationRoutingAddressImpl(cpnl);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
		
		//int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
	}
}
